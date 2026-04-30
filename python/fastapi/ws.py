from fastapi import APIRouter, WebSocket, websockets

router = APIRouter(prefix = "/ws")

@router.websocket("/connect")
async def wconnect(websocket: WebSocket):
    await websocket.accept()
    await websocket.send_text("Hello, FastAPI!")
    try:
        while True:
            data = await websocket.receive_text()
            await websocket.send_text(data)
    except:
        print(f"WebSocket closed {websocket.client_state}")
        if websocket.client_state == websockets.WebSocketState.CONNECTED:
            await websocket.close()
        else:
            pass
