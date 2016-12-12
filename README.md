# BTS
## セットアップ
```
./gradlew UiServer:npm_install
./gradlew UiServer:npm_build
```

## ビルド
```
./gradlew build
```

## 開発用にサーバーを起動する
1. ApiServerを起動
    ```
    ./gradlew ApiServer:bootRun
    ```
1. UiServerを起動
    ```
    ./gradlew UiServer:bootRun
    ```
1. webpack-dev-serverを起動
    ```
    cd UiServer/front
    npm start
    ```
1. http://localhost:3001/login/githubにアクセスして認証を通す
