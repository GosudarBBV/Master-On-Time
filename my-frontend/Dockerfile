# Базовий образ Node 20 (підтримує Vite та плагіни)
FROM node:20-alpine

# Робоча директорія всередині контейнера
WORKDIR /app

# Копіюємо package.json та package-lock.json
COPY package*.json ./

# Встановлюємо залежності
RUN npm install

# Копіюємо весь фронтенд у контейнер
COPY . .

# Збираємо React фронтенд
RUN npm run build

# Сервуємо статичні файли через serve
CMD ["npx", "serve", "-s", "dist", "-l", "$PORT"]

# Railway автоматично підставляє змінну $PORT
EXPOSE 4173