# azi build Makefile

# (fdroid) set gradlew
ifeq ($(BUILD),fdroid)
BIN_GRADLE := gradle
else
BIN_GRADLE := ./gradlew
endif

# fdroid build
all: azi-unzip npm-setup ui-loader azi-aar demo-apk
.PHONY: all

# 构建: azi-demo.apk
demo-apk: demo-apk-azi-aar demo-apk-init-zip
	cd demo-apk && $(BIN_GRADLE) assemble
.PHONY: demo-apk

#  从 azi-aar 复制编译好的 azi.aar 文件
demo-apk-azi-aar:
	mkdir -p demo-apk/app/libs
	cp azi-aar/libazi/build/outputs/aar/libazi-release.aar demo-apk/app/libs/azi.aar
.PHONY: demo-apk-azi-aar

#  使用 zip 生成 test-init.azi.zip
demo-apk-init-zip:
	mkdir -p demo-apk/app/src/main/assets
	cd demo-apk/test-init && zip ../test-init.azi.zip *
	mv demo-apk/test-init.azi.zip demo-apk/app/src/main/assets/
.PHONY: demo-apk-init-zip

# 构建: azi.aar
azi-aar:
	mkdir -p azi-aar/libazi/src/main/assets/azi
	cp azi-unzip/target/aarch64-linux-android/release/azi-unzip azi-aar/libazi/src/main/assets/azi/unzip
	cd azi-aar && $(BIN_GRADLE) assemble
.PHONY: azi-aar

# 构建: azi-unzip (aarch64-linux-android)
azi-unzip:
	export PATH=$$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin:$$PATH && \
	export CARGO_TARGET_AARCH64_LINUX_ANDROID_LINKER=aarch64-linux-android28-clang && \
	export AR_aarch64_linux_android=llvm-ar && \
	export CC_aarch64_linux_android=aarch64-linux-android28-clang && \
	cd azi-unzip && \
	cargo build --target aarch64-linux-android --release
.PHONY: azi-unzip

# npm install
npm-setup:
	cd ui-loader && npm install
.PHONY: npm-setup

# 构建: azi-loader
ui-loader:
	cd ui-loader && npm run build
	mkdir -p azi-aar/libazi/src/main/assets/azi/loader
	cp ui-loader/dist/index.html azi-aar/libazi/src/main/assets/azi/loader/
	mkdir -p azi-aar/libazi/src/main/assets/azi/loader/assets
	cp ui-loader/dist/assets/*.js azi-aar/libazi/src/main/assets/azi/loader/assets/
	cp ui-loader/dist/assets/*.css azi-aar/libazi/src/main/assets/azi/loader/assets/
.PHONY: ui-loader
