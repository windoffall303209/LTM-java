# Common Module

Module dùng chung giữa Server và Client, chứa các định nghĩa về giao thức và DTO.

## Cấu trúc

```
common/
├── src/main/java/com/auction/common/
│   ├── protocol/           # Định nghĩa giao thức
│   │   ├── MessageType.java    # Các loại message
│   │   ├── Protocol.java       # Build và parse message
│   │   └── MessageParser.java  # Parse message phức tạp
│   └── dto/                # Data Transfer Objects
│       ├── UserDTO.java
│       ├── AuctionDTO.java
│       ├── BidDTO.java
│       └── ResponseDTO.java
└── bin/                    # Compiled classes
```

## Compile

```bash
cd source/common
javac -d bin src/main/java/com/auction/common/**/*.java
jar cvf common.jar -C bin .
```

## Sử dụng

### Protocol Format

Tất cả messages theo format: `COMMAND|param1|param2|param3|...`

Ví dụ:
- `LOGIN|alice|password123`
- `BID|1|25000000`
- `BID_UPDATE|1|Bob|26000000|175`

### Build Message

```java
String msg = Protocol.buildMessage(MessageType.LOGIN, "alice", "pass123");
// Result: "LOGIN|alice|pass123"
```

### Parse Message

```java
String[] parts = Protocol.parseMessage("LOGIN|alice|pass123");
String command = parts[0];  // "LOGIN"
String username = parts[1]; // "alice"
String password = parts[2]; // "pass123"
```

## Dependencies

Không có dependencies bên ngoài, chỉ dùng Java standard library.

