# Use official Node.js base image
FROM node:20

# Set working directory
WORKDIR /app

# Copy dependency files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the app
COPY . .

# Expose Vite's default port
EXPOSE 5173

# Start the app in dev mode (Vite)
CMD ["npm", "run", "dev", "--", "--host"]
