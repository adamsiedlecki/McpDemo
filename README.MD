## Serwer MCP

### POST 10.0.0.5:8080/mcp

Body:
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2025-03-26",
    "capabilities": {
      "roots": {
        "listChanged": true
      },
      "sampling": {}
    },
    "clientInfo": {
      "name": "ExampleClient",
      "version": "1.0.0"
    }
  }
}
```

Zwraca:
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "result": {
        "protocolVersion": "2025-03-26",
        "capabilities": {
            "completions": {},
            "prompts": {
                "listChanged": false
            },
            "resources": {
                "subscribe": false,
                "listChanged": false
            },
            "tools": {
                "listChanged": false
            }
        },
        "serverInfo": {
            "name": "mcp-server",
            "version": "1.0.0"
        }
    }
}
```

### 10.0.0.5:8080/mcp

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list"
}
```

Zwraca

```json
{
    "jsonrpc": "2.0",
    "id": 2,
    "result": {
        "tools": [
            {
                "name": "employeeLeaveStatus",
                "description": "Sprawdza status urlopu pracownika",
                "inputSchema": {
                    "type": "object",
                    "properties": {
                        "name": {
                            "type": "string"
                        },
                        "employees": {
                            "type": "array",
                            "items": {
                                "type": "object",
                                "properties": {
                                    "name": {
                                        "type": "string"
                                    },
                                    "usedLeaveDays": {
                                        "type": "integer",
                                        "format": "int32"
                                    },
                                    "yearsOfService": {
                                        "type": "integer",
                                        "format": "int32"
                                    }
                                },
                                "required": [
                                    "name",
                                    "usedLeaveDays",
                                    "yearsOfService"
                                ]
                            }
                        }
                    },
                    "required": [
                        "name",
                        "employees"
                    ],
                    "additionalProperties": false
                }
            },
            {
                "name": "employeesWithoutLeave",
                "description": "Zwraca listę pracowników, którzy nie mają już urlopu",
                "inputSchema": {
                    "type": "object",
                    "properties": {
                        "employees": {
                            "type": "array",
                            "items": {
                                "type": "object",
                                "properties": {
                                    "name": {
                                        "type": "string"
                                    },
                                    "usedLeaveDays": {
                                        "type": "integer",
                                        "format": "int32"
                                    },
                                    "yearsOfService": {
                                        "type": "integer",
                                        "format": "int32"
                                    }
                                },
                                "required": [
                                    "name",
                                    "usedLeaveDays",
                                    "yearsOfService"
                                ]
                            }
                        }
                    },
                    "required": [
                        "employees"
                    ],
                    "additionalProperties": false
                }
            }
        ]
    }
}
```