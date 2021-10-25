# The first stage
# Build React static files
FROM node:16.1-alpine as build

WORKDIR /cv-indexer
COPY . .
RUN npm i
RUN npm run build
RUN npm install -g serve

CMD ["serve", "-s", "build"]
EXPOSE 5000