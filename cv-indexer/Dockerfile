FROM node:16-alpine3.14 as build
LABEL maintainer="alexandre.em@pm.me"

WORKDIR /cv-indexer
COPY . .
RUN npm i
RUN npm run build
RUN npm install -g serve

CMD ["serve", "-s", "build"]
EXPOSE 5000