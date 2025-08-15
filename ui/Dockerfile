# Etapa 1: Build do Angular
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Etapa 2: Servir com Nginx
FROM nginx:alpine
COPY --from=build /app/dist/costelata-app /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

# Healthcheck
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD curl -f http://localhost || exit 1
