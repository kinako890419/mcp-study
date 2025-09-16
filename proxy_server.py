import os
from fastmcp import FastMCP
from fastmcp.client import StreamableHttpTransport
from fastmcp.server.proxy import ProxyClient


REMOTE_MCP_URL = "http://127.0.0.1:8000/mcp"
TOKEN = os.getenv("API_TOKEN")
headers = {"Authorization": f"Bearer {TOKEN}"} if TOKEN else {}

transport = StreamableHttpTransport(
    url=REMOTE_MCP_URL,
    headers={"Authorization": f"Bearer {TOKEN}"} if TOKEN else {}
)


proxy = FastMCP.as_proxy(
    ProxyClient(transport=transport),
    name="OpenAPI MCP Proxy"
)

if __name__ == "__main__":
    proxy.run()
