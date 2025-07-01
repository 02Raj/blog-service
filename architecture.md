# Devblog Project Architecture

This document outlines the project structure for the Devblog application.
echo "# Deploy Trigger"
```
devblog/
├── .gitattributes
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── HELP.md
├── rm
├── settings.gradle
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── learnwithme/
    │   │           └── blog/
    │   │               └── devblog/
    │   │                   ├── LearnWithMeBlogApplication.java
    │   │                   ├── config/
    │   │                   │   ├── CloudinaryConfig.java
    │   │                   │   ├── MongoConfig.java
    │   │                   │   ├── S3Config.java
    │   │                   │   ├── SwaggerConfig.java
    │   │                   │   └── WebSecurityConfig.java
    │   │                   ├── controller/
    │   │                   │   ├── AuthController.java
    │   │                   │   ├── BlogPostController.java
    │   │                   │   ├── CategoryController.java
    │   │                   │   └── UserController.java
    │   │                   ├── dto/
    │   │                   │   ├── ApiResponseDto.java
    │   │                   │   ├── AuthRequestDto.java
    │   │                   │   ├── AuthResponseDto.java
    │   │                   │   ├── BlogPostDto.java
    │   │                   │   ├── CategoryDto.java
    │   │                   │   ├── PageResponseDto.java
    │   │                   │   ├── PasswordChangeDto.java
    │   │                   │   ├── SubcategoryDto.java
    │   │                   │   └── UserDto.java
    │   │                   ├── exception/
    │   │                   │   ├── ApiException.java
    │   │                   │   ├── BadRequestException.java
    │   │                   │   ├── GlobalExceptionHandler.java
    │   │                   │   ├── ResourceNotFoundException.java
    │   │                   │   └── UnauthorizedException.java
    │   │                   ├── model/
    │   │                   │   ├── BlogPost.java
    │   │                   │   ├── Category.java
    │   │                   │   ├── Subcategory.java
    │   │                   │   └── User.java
    │   │                   ├── repository/
    │   │                   │   ├── BlogPostRepository.java
    │   │                   │   ├── CategoryRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   ├── security/
    │   │                   │   ├── CustomUserDetailsService.java
    │   │                   │   ├── JwtAuthenticationEntryPoint.java
    │   │                   │   ├── JwtAuthenticationFilter.java
    │   │                   │   └── JwtTokenProvider.java
    │   │                   ├── service/
    │   │                   │   ├── impl/
    │   │                   │   │   ├── AuthServiceImpl.java
    │   │                   │   │   ├── BlogPostServiceImpl.java
    │   │                   │   │   ├── CategoryServiceImpl.java
    │   │                   │   │   ├── S3ServiceImpl.java
    │   │                   │   │   └── UserServiceImpl.java
    │   │                   │   ├── AuthService.java
    │   │                   │   ├── BlogPostService.java
    │   │                   │   ├── CategoryService.java
    │   │                   │   ├── S3Service.java
    │   │                   │   └── UserService.java
    │   │                   └── util/
    │   │                       ├── S3Utils.java
    │   │                       └── SlugUtil.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/
                └── learnwithme/
                    └── blog/
                        └── devblog/
                            └── LearnWithMeBlogApplicationTests.java