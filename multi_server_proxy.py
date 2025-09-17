import os
from fastmcp import FastMCP


# Multi-server configuration
config = {
    "mcpServers": {
        "weather-server": {
            "url": "http://127.0.0.1:8001/mcp",
            "transport": "http"
        },
        "project-task-management": {
            "url": "http://127.0.0.1:8002/mcp",
            "transport": "http",
            "headers": {"Authorization": f"Bearer {os.getenv('API_TOKEN')}"} # get api token from client config file
        }
    }
}


proxy = FastMCP.as_proxy(config, name="Multi Server MCP Proxy")


if __name__ == "__main__":
    # proxy.run(transport="streamable-http", port=8003)
    proxy.run() # stdio