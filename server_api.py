from typing import Any, Coroutine

import httpx
from fastmcp import FastMCP, Context
from fastmcp.server.dependencies import get_http_headers
from httpx import HTTPError, HTTPStatusError

API_BASE_URL = "http://localhost:8888"
mcp = FastMCP("API server MCP")


async def make_api_request(url: str, method: str = "GET", data: dict = None) -> dict:
    """Make an HTTP request to the API."""

    headers = get_http_headers()  # Get headers from the current request context

    async with httpx.AsyncClient(base_url=API_BASE_URL) as client:
        client.headers.update(headers)

        if method.upper() == "GET":
            response = await client.get(url)
        elif method.upper() == "POST":
            response = await client.post(url, json=data)
        elif method.upper() == "PUT":
            response = await client.put(url, json=data)
        elif method.upper() == "DELETE":
            response = await client.delete(url)
        else:
            raise ValueError(f"Unsupported HTTP method: {method}")

        response.raise_for_status()
        return response.json()


@mcp.tool(
    description="Get all users, optionally filtering by deletion status (ADMIN). "
)
async def get_all_users(ctx: Context, is_deleted: str | None = None):
    await ctx.info("Fetching all users from the API...")  # show in mcp inspector server notification
    params = {}
    if is_deleted is not None:
        params["isDeleted"] = is_deleted
    try:
        return await make_api_request("/users", method="GET", data=params)
    except HTTPStatusError as e:
        await ctx.error(f"HTTP status error occurred: {e.response.status_code} - {e.response.text}")

if __name__ == "__main__":
    mcp.run(transport="streamable-http")
