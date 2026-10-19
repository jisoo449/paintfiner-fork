# Step 1: 리액트 애플리케이션 빌드
From node:20 AS build
WORKDIR /app
COPY package*.json ./
COPY .env .env
RUN npm ci --only=production --no-audit --no-fund
COPY . .
RUN npm run build && npm cache clean --force

# Step 2. Nginx 설정 및 리액트 애플리케이션 serve
FROM nginx:1.25.0-alpine as production
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
