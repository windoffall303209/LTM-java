# 🚀 HƯỚNG DẪN PUSH FOUNDATION CODE LÊN GIT

Đây là hướng dẫn từng bước để push foundation code lên repository.

## 📋 CHUẨN BỊ

Đảm bảo bạn đã:
- [ ] Cài đặt Git
- [ ] Có repository trên GitHub/GitLab
- [ ] Đã clone repository về máy (hoặc đang ở trong folder dự án)

---

## 🔄 CÁCH 1: PUSH TỪ FOLDER FOUNDATION-CODE

### Bước 1: Copy files vào project chính

Có 2 cách:

**Cách A - Copy thủ công:**
```bash
# Copy toàn bộ nội dung từ foundation-code vào project gốc
# Windows:
xcopy foundation-code\* ClientServer\ /E /H /Y

# Linux/Mac:
cp -r foundation-code/* ClientServer/
```

**Cách B - Di chuyển từng folder:**
```bash
# Di chuyển server folder
move foundation-code\server ClientServer\source\server

# Di chuyển client folder
move foundation-code\client ClientServer\source\client

# Di chuyển các file khác
move foundation-code\.gitignore ClientServer\
move foundation-code\README.md ClientServer\FOUNDATION_README.md
```

### Bước 2: Vào thư mục project chính
```bash
cd ClientServer
```

### Bước 3: Kiểm tra status
```bash
git status
```

Bạn sẽ thấy danh sách các file mới và file đã sửa.

### Bước 4: Add các file foundation

**Option 1 - Add tất cả (nhanh nhưng ít kiểm soát):**
```bash
git add .
```

**Option 2 - Add từng phần (khuyến nghị):**
```bash
# Backend foundation
git add source/server/pom.xml
git add source/server/src/main/resources/application.properties
git add source/server/src/main/java/com/auction/AuctionSystemApplication.java
git add source/server/src/main/java/com/auction/config/
git add source/server/src/main/java/com/auction/model/
git add source/server/src/main/java/com/auction/repository/
git add source/server/src/main/java/com/auction/dto/
git add source/server/src/main/java/com/auction/service/CustomUserDetailsService.java

# Frontend foundation
git add source/client/package.json
git add source/client/public/index.html
git add source/client/public/login.html
git add source/client/public/register.html
git add source/client/public/css/style.css
git add source/client/public/js/config.js
git add source/client/public/js/auth.js

# Other files
git add .gitignore
git add FOUNDATION_README.md
```

### Bước 5: Commit
```bash
git commit -m "chore: setup project foundation

- Add Spring Boot configuration and dependencies
- Add core entities (User, Auction)
- Add repositories and DTOs
- Add security, CORS, and WebSocket configuration
- Add frontend foundation (login, register, basic styling)
- Add authentication utilities
- Setup H2 database for development

This is the foundation code for team members to start working on their features.
Team members should pull this and create their feature branches."
```

### Bước 6: Push lên remote
```bash
# Nếu đang ở branch main/master
git push origin main

# Hoặc nếu branch khác
git push origin <tên-branch>
```

---

## 🔄 CÁCH 2: PUSH TRỰC TIẾP TỪ FOUNDATION-CODE (Tạo repo mới)

Nếu bạn muốn tạo repository hoàn toàn mới từ foundation-code:

### Bước 1: Vào folder foundation-code
```bash
cd foundation-code
```

### Bước 2: Initialize git
```bash
git init
```

### Bước 3: Add remote repository
```bash
git remote add origin <URL-repository-của-bạn>

# Ví dụ:
# git remote add origin https://github.com/username/auction-system.git
```

### Bước 4: Add tất cả files
```bash
git add .
```

### Bước 5: Commit
```bash
git commit -m "chore: initial project foundation setup"
```

### Bước 6: Push
```bash
# Tạo branch main và push
git branch -M main
git push -u origin main
```

---

## ✅ SAU KHI PUSH - HƯỚNG DẪN CHO TEAM

### Thông báo cho team pull code:

**Message mẫu gửi team:**
```
🎉 Foundation code đã được push lên main branch!

📥 Các bạn hãy pull về và bắt đầu làm việc:

1. Pull latest code:
   git checkout main
   git pull origin main

2. Tạo branch riêng:
   - Người 1: git checkout -b feature/admin-panel
   - Người 2: git checkout -b feature/user-bidding
   - Người 3: git checkout -b feature/core-functionality

3. Test foundation code chạy được:
   Backend:
   - cd source/server
   - mvn clean install
   - mvn spring-boot:run

   Frontend:
   - cd source/client
   - npm install
   - npm start

4. Đọc README.md và WORK_DIVISION_PLAN.md để biết nhiệm vụ

5. Bắt đầu code! 🚀

Có vấn đề gì ping tôi nhé!
```

---

## 📝 KIỂM TRA SAU KHI PUSH

### Trên GitHub/GitLab:
- [ ] Vào repository, kiểm tra files đã có
- [ ] Kiểm tra commit message hiển thị đúng
- [ ] Kiểm tra cấu trúc folder đúng

### Yêu cầu team test:
```bash
# Mỗi thành viên thử pull và chạy
git clone <repo-url>
cd <project-folder>

# Test backend
cd source/server
mvn spring-boot:run
# Truy cập http://localhost:8080

# Test frontend (terminal khác)
cd source/client
npm install
npm start
# Truy cập http://localhost:5500
```

---

## 🚨 XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi: "Updates were rejected"
```bash
# Pull trước khi push
git pull origin main
# Resolve conflicts nếu có
git push origin main
```

### Lỗi: "Permission denied"
```bash
# Kiểm tra authentication
git remote -v

# Nếu dùng HTTPS, kiểm tra token/password
# Nếu dùng SSH, kiểm tra SSH key
```

### Lỗi: "File too large"
```bash
# Kiểm tra .gitignore đã exclude node_modules, target/
# Xóa cache và add lại
git rm -r --cached .
git add .
git commit -m "fix: update gitignore"
```

### Files không được track
```bash
# Kiểm tra .gitignore
cat .gitignore

# Force add nếu cần
git add -f <file-path>
```

---

## 📊 VERIFY FOUNDATION CODE

Checklist để đảm bảo foundation code đầy đủ:

### Backend:
- [ ] `pom.xml` - Maven dependencies
- [ ] `application.properties` - Database config
- [ ] `AuctionSystemApplication.java` - Main class
- [ ] `config/` - 4 config files (Security, Web, WebSocket, DataInitializer)
- [ ] `model/` - User.java, Auction.java
- [ ] `repository/` - UserRepository.java, AuctionRepository.java
- [ ] `dto/` - 5 DTO files
- [ ] `service/` - CustomUserDetailsService.java

### Frontend:
- [ ] `package.json`
- [ ] `index.html`, `login.html`, `register.html`
- [ ] `css/style.css`
- [ ] `js/config.js`, `js/auth.js`

### Other:
- [ ] `.gitignore`
- [ ] `README.md`

### Test Backend chạy được:
```bash
cd source/server
mvn clean install
# Should build successfully

mvn spring-boot:run
# Should start on port 8080
# Check http://localhost:8080/h2-console
```

### Test Frontend chạy được:
```bash
cd source/client
npm install
npm start
# Should open browser at localhost:5500
```

---

## 🎯 NEXT STEPS SAU KHI PUSH

1. **Tag this commit (optional but recommended):**
```bash
git tag -a v0.1-foundation -m "Foundation code for team"
git push origin v0.1-foundation
```

2. **Create branch protection rules (on GitHub/GitLab):**
- Require pull request reviews
- Require status checks to pass
- Don't allow force push to main

3. **Setup project board/issues (optional):**
- Create issues for each major task
- Assign to team members
- Setup milestones

4. **Schedule kick-off meeting:**
- Review foundation code together
- Clarify any questions
- Confirm task assignments
- Set up communication channels

---

## 📞 SUPPORT

Nếu gặp vấn đề:
1. Kiểm tra Git status: `git status`
2. Kiểm tra Git log: `git log --oneline`
3. Hỏi team lead
4. Search trên Google/StackOverflow

---

**Chúc team push thành công và code vui vẻ! 🎉**
