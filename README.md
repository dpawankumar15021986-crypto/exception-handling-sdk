# Exception Handling SDK

A comprehensive Java SDK for standardized exception handling across checked, unchecked, errors, and HTTP status codes. This SDK provides a unified approach to exception management with configurable handling strategies, structured logging, and consistent error responses.

## Features

- **Comprehensive Exception Hierarchy**: Base exception classes with built-in error IDs, timestamps, and context
- **Multiple Exception Types**: Support for checked, unchecked, system errors, and HTTP status codes
- **Configurable Handlers**: Flexible exception handling with customizable strategies
- **Structured Logging**: Integrated logging with SLF4J and contextual information
- **Error Response Models**: Standardized error response formats with JSON serialization
- **Context Management**: Thread-safe context storage for additional error metadata
- **Multiple Output Formats**: JSON, XML, plain text, and structured map formats
- **HTTP Status Code Support**: Complete HTTP status code enumeration with categorization
- **Builder Patterns**: Fluent APIs for configuration and exception creation

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>com.sdk</groupId>
    <artifactId>exception-handling-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
