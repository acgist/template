<template>
  <div class="icons-container">
    <el-tabs type="border-card">
      <el-tab-pane label="Acgist">
        <div class="grid">
          <div v-for="item of acgistIcons" :key="item">
            <el-tooltip placement="top">
              <div slot="content">{{ generateAcgistIcons(item) }}</div>
              <div class="icon-item">
                <i :class="'acgist-' + item" />
                <span>{{ item }}</span>
              </div>
            </el-tooltip>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Svg">
        <div class="grid">
          <div v-for="item of svgIcons" :key="item">
            <el-tooltip placement="top">
              <div slot="content">{{ generateSvgIcons(item) }}</div>
              <div class="icon-item">
                <svg-icon :icon-class="item" class-name="disabled" />
                <span>{{ item }}</span>
              </div>
            </el-tooltip>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Element">
        <div class="grid">
          <div v-for="item of elementIcons" :key="item">
            <el-tooltip placement="top">
              <div slot="content">{{ generateElementIcons(item) }}</div>
              <div class="icon-item">
                <i :class="'el-icon-' + item" />
                <span>{{ item }}</span>
              </div>
            </el-tooltip>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import svgIcons from './svg';
import acgistIcons from './acgist';
import elementIcons from './element';

export default {
  name: 'Icons',
  data() {
    return {
      svgIcons,
      acgistIcons,
      elementIcons
    };
  },
  methods: {
    generateSvgIcons(symbol) {
      return `<svg-icon icon-class="${symbol}" />`;
    },
    generateAcgistIcons(symbol) {
      return `<i class="acgist-${symbol}" />`;
    },
    generateElementIcons(symbol) {
      return `<i class="el-icon-${symbol}" />`;
    }
  }
};
</script>

<style lang="scss" scoped>
.icons-container {
  margin: 10px 20px 0;
  overflow: hidden;

  .grid {
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }

  .icon-item {
    margin: 20px;
    height: 85px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 30px;
    color: #24292e;
    cursor: pointer;
  }

  span {
    display: block;
    font-size: 16px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
}
</style>
