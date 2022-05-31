FROM node:18-alpine3.14 as build
WORKDIR /cv-indexer

# Allows to cache the dependencies and to not reinstall them each time we build the app
COPY package.json .
RUN npm i

# Copying the project then building it
COPY . .
RUN npm run build

# Serve the built app on a nginx server
FROM nginx:stable-alpine
COPY --from=build /cv-indexer/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

