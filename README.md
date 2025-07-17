# Social Network Skeleton

This project is part of a Highload development course.

## Getting Started (IntelliJ IDEA + Environment Setup)

1. **Clone the repository**
   ```bash
    git clone git@github.com:devcampp/socials.git
    cd socials
    ```
2. **Open the project in IntelliJ IDEA**
    * Go to ```Edit Configurations...```
    * Set active profiles `dev`
    * _This project uses a `.env` file located in `dev/.env` to manage environment variables shared across Docker and
      the application runtime._ Set env file from `dev/.env`

3. **Run Dev Infrastructure**
    * Open terminal in the root of the project
    * Navigate to the `dev` folder: `cd dev`
    * Make sure the .env file exists and contains the required variables
    * Start the infrastructure with: `docker-compose up -d`

## Services Overview

| Service     | Host Port | Container Port | Username  | Password  | Notes                                           |
|-------------|-----------|----------------|-----------|-----------|-------------------------------------------------|
| Postgresql  | 5432      | 5432           | `socials` | `socials` | Database: `socials`, Schema: `socials`          |
| Application | 8080      |                |           |           | Sign-in url: localhost:8080/api/v1/auth/sign-in |
