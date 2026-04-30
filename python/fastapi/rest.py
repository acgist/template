from fastapi import APIRouter

router = APIRouter(prefix = "/user", tags = [ "用户管理" ])

@router.get("/list", name = "list", summary = "获取所有用户")
async def list():
    return [{"id": 1, "name": "Alice"}]

@router.get("/{id}", name = "detail", summary = "获取用户详情")
async def detail(id: int):
    return {"id": id, "name": f"User {id}"}
