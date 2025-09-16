import os
from fastmcp import FastMCP
from fastmcp.server.proxy import ProxyClient


REMOTE_MCP_URL = "http://127.0.0.1:8000/mcp"
TOKEN = os.getenv("API_TOKEN")
headers = {"Authorization": f"Bearer {TOKEN}"} if TOKEN else {}


proxy = FastMCP.as_proxy(
    ProxyClient(REMOTE_MCP_URL),
    name="OpenAPI MCP Proxy"
)

if __name__ == "__main__":
    proxy.run()
