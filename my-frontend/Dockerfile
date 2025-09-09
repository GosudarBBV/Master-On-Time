# ========================
# Dockerfile для React фронтенду
# ========================

FROM node:20-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run buildф
CMD ["npx", "serve", "-s", "dist", "-l", "$PORT"]
EXPOSE $PORT