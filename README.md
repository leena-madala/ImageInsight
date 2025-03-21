
# Image Insight REST API

## Overview
This project is a RESTful web service built using Java Spring Boot, H2 (in-memory database for development), and PostgreSQL (for production readiness). It enables users to provide an image URL, detect objects in that image, and store the image metadata in the database for future retrieval. Users can also search for images containing specific objects, which were detected while uploading the image.

---

## API Endpoints

### 1. **Upload Images**
- **Endpoint**: `POST /images`
- **Description**: Allows users to upload an image by providing a URL, an optional label, and a flag to enable object detection.
- **Response**: Returns a JSON response with the image metadata, including:
  - Generated label (if none was provided)
  - Unique identifier
  - List of objects detected in the image (if object detection is enabled)

**Example cURL Request**:
```bash
curl --location 'http://localhost:8080/image-insight/images' --header 'Content-Type: application/json' --data '{
    "url": "https://imagga.com/static/images/tagging/wind-farm-538576_640.jpg",
    "label": "Wind farm"
}'
```

---

### 2. **Retrieve All Images**
- **Endpoint**: `GET /images`
- **Description**: Fetches metadata for all uploaded images.
- **Response**: Returns a JSON array containing metadata for each image.

**Example cURL Request**:
```bash
curl --location 'http://localhost:8080/image-insight/images'
```

---

### 3. **Retrieve an Image by ID**
- **Endpoint**: `GET /images/{imageId}`
- **Description**: Retrieves metadata for a specific image by its unique identifier.
- **Response**: Returns a JSON object with the metadata of the requested image.

**Example cURL Request**:
```bash
curl --location 'http://localhost:8080/image-insight/images/1'
```

---

### 4. **Search Images by Detected Objects**
- **Endpoint**: `GET /images?objects=object1,object2,...`
- **Description**: Retrieves images whose metadata includes specific detected objects provided as query parameters.
- **Response**: Returns a JSON array containing metadata for matching images.

**Example cURL Request**:
```bash
curl --location 'http://localhost:8080/image-insight/images?objects=turbine,spring'
```

---

## API Error Handling
Comprehensive error handling ensures proper communication of issues:
- `400 Bad Request`: For client-side errors (e.g., invalid input)
- `404 Not Found`: For resources not found
- `500 Internal Server Error`: For server-side failures

---

## Technical Details

### **Technologies Used**
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: H2 (in-memory database for development), PostgreSQL (for production)
- **Object Detection**: Integrated with [Imagga](https://imagga.com/) for image analysis
- **Testing**: JUnit 5 for unit and integration tests

---

## Prerequisites

- **Java 17** or higher
- **Maven** (for build and dependency management)
- **Docker** (for containerized deployments)

---

## Getting Started

### **Option 1: Run Locally Using Maven**

#### **Clone the Repository**
```bash
git clone https://github.com/leena-recruiting/leena-madala.git
cd leena-madala/ImageInsight
```

#### **Build and Run the Application**
- **Build the project**:
```bash
mvn clean install
```
- **Run the application**:
```bash
mvn spring-boot:run
```

#### **Access the Application**
The service will be accessible at: `http://localhost:8080/image-insight`

### **Option 2: Run Using Docker**

#### **Clone the Repository**
```bash
git clone https://github.com/leena-recruiting/leena-madala.git
cd leena-madala/ImageInsight
```

#### **Ensure Docker Engine is Running**
Ensure that the Docker Engine is up and running on your system before proceeding.

#### **Build the Docker Image**
```bash
docker build -t imageinsight .
```

#### **Run the Docker Container**
```bash
docker run -p 8080:8080 imageinsight
```

#### **Access the Application**
The service will be accessible at: `http://localhost:8080/image-insight`

#### **Stop the Docker Container**
- To list the running containers:
```bash
docker ps
```
- To stop the container:
```bash
docker stop <container_id>
```

---

## Future Improvements
- **Authentication**: Implement security features, including API authentication and authorization.
- **Cucumber Integration**: Replace the current end-to-end test implementation with Cucumber. It offers enhanced workflow support and improved readability.

