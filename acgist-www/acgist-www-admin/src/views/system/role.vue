<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole">新建角色</el-button>

    <el-table :data="rolesList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="角色标识" width="220">
        <template slot-scope="scope">{{ scope.row.token }}</template>
      </el-table-column>
      <el-table-column align="center" label="角色名称" width="220">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column align="header-center" label="角色描述">
        <template slot-scope="scope">{{ scope.row.memo }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑角色':'新建角色'">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="标识">
          <el-input v-model="role.token" placeholder="角色标识" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="role.name" placeholder="角色名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="role.memo"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="角色描述"
          />
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            ref="tree"
            :check-strictly="checkStrictly"
            :data="routesData"
            :props="defaultProps"
            show-checkbox
            node-key="path"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmRole">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import path from 'path';
import { deepClone } from '@/utils';
import {
  getRoutes,
  getRoles,
  addRole,
  updateRole,
  deleteRole
} from '@/api/role';

const defaultRole = {
  key: '',
  name: '',
  memo: '',
  routes: []
};

export default {
  data() {
    return {
      role: Object.assign({}, defaultRole),
      routes: [],
      rolesList: [],
      dialogVisible: false,
      dialogType: 'new',
      checkStrictly: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      }
    };
  },
  computed: {
    routesData() {
      return this.routes;
    }
  },
  created() {
    this.getRoutes();
    this.getRoles();
  },
  methods: {
    async getRoutes() {
      const res = await getRoutes();
      this.serviceRoutes = res.data;
      this.routes = this.generateRoutes(res.data);
    },
    async getRoles() {
      const res = await getRoles();
      this.rolesList = res.data;
    },

    generateRoutes(routes, basePath = '/') {
      const res = [];
      for (let route of routes) {
        if (route.hidden) {
          continue;
        }
        const onlyOneShowingChild = this.onlyOneShowingChild(
          route.children,
          route
        );
        if (route.children && onlyOneShowingChild && !route.alwaysShow) {
          route = onlyOneShowingChild;
        }
        const data = {
          path: path.resolve(basePath, route.path),
          title: route.meta && route.meta.title
        };
        if (route.children) {
          data.children = this.generateRoutes(route.children, data.path);
        }
        res.push(data);
      }
      return res;
    },
    generateArr(routes) {
      let data = [];
      routes.forEach(route => {
        data.push(route);
        if (route.children) {
          const temp = this.generateArr(route.children);
          if (temp.length > 0) {
            data = [...data, ...temp];
          }
        }
      });
      return data;
    },
    handleAddRole() {
      this.role = Object.assign({}, defaultRole);
      if (this.$refs.tree) {
        this.$refs.tree.setCheckedNodes([]);
      }
      this.dialogType = 'new';
      this.dialogVisible = true;
    },
    handleEdit(scope) {
      this.dialogType = 'edit';
      this.dialogVisible = true;
      this.checkStrictly = true;
      this.role = deepClone(scope.row);
      this.$nextTick(() => {
        const routes = this.generateRoutes(this.role.routes);
        this.$refs.tree.setCheckedNodes(this.generateArr(routes));
        this.checkStrictly = false;
      });
    },
    handleDelete({ $index, row }) {
      this.$confirm(`是否删除角色：${row.name}`, '警告', {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await deleteRole(row.token);
          this.rolesList.splice($index, 1);
          this.$message({
            type: 'success',
            message: '删除成功'
          });
        })
        .catch(err => {
          console.error(err);
        });
    },
    generateTree(routes, basePath = '/', checkedKeys) {
      const res = [];

      for (const route of routes) {
        const routePath = path.resolve(basePath, route.path);

        // recursive child routes
        if (route.children) {
          route.children = this.generateTree(
            route.children,
            routePath,
            checkedKeys
          );
        }

        if (
          checkedKeys.includes(routePath) ||
          (route.children && route.children.length >= 1)
        ) {
          res.push(route);
        }
      }
      return res;
    },
    async confirmRole() {
      const isEdit = this.dialogType === 'edit';

      const checkedKeys = this.$refs.tree.getCheckedKeys();
      this.role.routes = this.generateTree(
        deepClone(this.serviceRoutes),
        '/',
        checkedKeys
      );

      if (isEdit) {
        await updateRole(this.role.token, this.role);
        for (let index = 0; index < this.rolesList.length; index++) {
          if (this.rolesList[index].token === this.role.token) {
            this.rolesList.splice(index, 1, Object.assign({}, this.role));
            break;
          }
        }
      } else {
        const { data } = await addRole(this.role);
        this.role.token = data.token;
        this.rolesList.push(this.role);
      }

      const { memo, token, name } = this.role;
      this.dialogVisible = false;
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>角色标识: ${token}</div>
            <div>角色名称: ${name}</div>
            <div>角色描述: ${memo}</div>
          `,
        type: 'success'
      });
    },

    onlyOneShowingChild(children = [], parent) {
      let onlyOneChild = null;
      const showingChildren = children.filter(item => !item.hidden);
      if (showingChildren.length === 1) {
        onlyOneChild = showingChildren[0];
        onlyOneChild.path = path.resolve(parent.path, onlyOneChild.path);
        return onlyOneChild;
      }
      if (showingChildren.length === 0) {
        onlyOneChild = { ...parent, path: '', noShowingChildren: true };
        return onlyOneChild;
      }

      return false;
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
