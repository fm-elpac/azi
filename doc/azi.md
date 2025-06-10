# 胖喵安初 (azi) 主要设计

主要目录及模块:

- azi-aar (`azi.aar`)

- azi-unzip (`unzip.zip`)

- ui-loader (`azi-loader.zip`)

- demo-apk (`azi-demo.apk`)

## Azi 环境变量

指向对应的目录 (path), 在执行 shell 命令时作为环境变量可访问.

- `AZI_DIR_APP_DATA`

  内部的应用数据文件存储 (外部无法访问) `/data/data/APP_ID/files`

- `AZI_DIR_SDCARD_DATA`

  sdcard 上的应用文件存储 (使用系统文件管理器可以访问)
  `/sdcard/Andorid/data/APP_ID/files`

- `AZI_DIR_SDCARD_CACHE`

  sdcard 上的缓存文件 (清除缓存可以删除) `/sdcard/Android/data/APP_ID/cache`

比如:

```
System.out: AziSh.env AZI_DIR_APP_DATA /data/user/0/io.github.fm_elpac.azi_demo/files
System.out: AziSh.env AZI_DIR_SDCARD_DATA /storage/emulated/0/Android/data/io.github.fm_elpac.azi_demo/files
System.out: AziSh.env AZI_DIR_SDCARD_CACHE /storage/emulated/0/Android/data/io.github.fm_elpac.azi_demo/cache
```

重要文件:

- `AZI_DIR_SDCARD_DATA/azi/azi_ok`

- `AZI_DIR_SDCARD_DATA/target_dir/azi_init.sh`

- `AZI_DIR_SDCARD_CACHE/azi_log/T-o|e.txt`

- `AZI_DIR_APP_DATA/azi/unzip` (chmod +x)

- `assets/azi/unzip`

- `assets/azi/loader/index.html`

- `assets/APP_INIT.azi.zip`

## 应用初始化过程

- (1.1) 应用 (Android app) 启动 (比如打开 `MainActivity` 主界面).

  - (1.2) 应用调用 `Azi.initZip(asset_zip, target_dir)` 进行初始化.

- (2.0) azi 检查 `AZI_DIR_SDCARD_DATA/target_dir` 是否存在, 如果存在, 跳过 zip
  解包过程.

  - (2.1) `target_dir` 不存在: azi 从 `assets/azi/unzip` 复制 (释放) 到
    `AZI_DIR_APP_DATA/azi/unzip`, 并设置可执行 (`chmod +x`).

  - (2.2) azi 从 `assets/asset_zip` 复制 (释放) 到
    `AZI_DIR_SDCARD_CACHE/asset_zip` (如果目标文件不存在).

  - (2.3) azi 调用上面的 `unzip` 可执行程序, 解压 `asset_zip` 文件到
    `target_dir`.

- (3.0) azi 检查 `AZI_DIR_SDCARD_DATA/azi/azi_ok` 文件是否存在, 如果存在, 跳过
  初始化脚本执行过程.

  - (3.1) azi 检查 `target_dir/azi_init.sh` 文件是否存在, 如果不存在, 跳过
    初始化脚本执行过程.

  - (3.2) azi 调用 `/system/bin/sh` 执行 `azi_init.sh` 脚本, 执行成功 (退出码 0)
    后, 创建 `azi_ok` 文件 (防止重复执行初始化脚本).

- (4) `Azi.initZip()` 初始化完毕, 调用完成回调接口.

初始化过程, 以及执行命令的过程 (stdout/stderr) 会写入日志文件
`AZI_DIR_SDCARD_CACHE/azi_log/T-o|e.txt`

## 设计目标

胖喵安初 (azi) 的主要设计目标如下:

- (1) **首次运行应用时, 自动执行 (一次) 初始化过程**
  (从而避免用户手动安装/初始化).

  具体实现: 检查 `target_dir` 是否存在, 存在则跳过, 不存在则执行初始化 (从
  assets 中解包释放 zip 文件).

- (2) **提供简单灵活丰富的初始化能力**.

  具体实现: zip 解包 (文件释放) 功能, 然后检查并执行 `azi_init.sh` 脚本,
  同时传递 `AZI_DIR_*` 环境变量. 在脚本中可以灵活进行初始化操作.

  脚本成功执行一次后, 创建 `azi_ok` 文件, 避免多次重复执行初始化脚本.

  app 只需将初始化 zip 放到 assets 中即可, 简化了下游应用集成初始化系统的过程.

- (3) **方便开发调试, 方便备份文件, 方便用户查看/修改文件,
  方便升级/更新资源文件**.

  具体实现: 绝大部分数据放在 `sdcard/Android/data` 中, 只有被迫情况下 (Android
  系统限制), 才使用 `/data/data` 位置.

  用户只需手动删除 `target_dir` 文件, 即可触发重新初始化. 用户删除 `azi_ok`
  文件, 即可触发重新执行初始化脚本.

  资源文件放在 sdcard 中, 只需替换文件即可实现升级/更新, 无需重新安装 apk.

  初始化/命令执行过程, 记录在 `azi_log` 日志文件中, 方便调试及故障排查.

- (4) 提供用户友好的界面.

  具体实现: azi 有 `ui-loader` 模块, 初始化时可向用户显示友好界面,
  并能显示错误信息.

## Azi 接口 (类) API (kotlin/java)

package: `io.github.fm_elpac.azi`

- `Azi`

  静态方法调用 (包装).

  - `Azi.setContext(context)`

    在 app `Application` 初始化时调用 (初始化).

  - `Azi.initZip(asset_zip, target_dir)`

    初始化入口函数 (常用).

  - `Azi.env(name)`

    获取 azi 环境变量的值: `AZI_DIR_APP_DATA`, `AZI_DIR_SDCARD_DATA`,
    `AZI_DIR_SDCARD_CACHE`

  - `Azi.log(text)`

    记录 (写入) 日志.

  - `Azi.sh(cmd)`

    调用 `/system/bin/sh` 执行命令.

  - `Azi.cpAsset(name, target)`

    从 `apk/assets` 中复制 (释放) 文件.

- (interface) `AziCb`

  初始化完成的回调接口.

- `AziApi`

  接口的具体实现 (`Azi` 调用 `AziApi`).

- `AziLog`

  日志记录功能.

- `AziSh`

  调用 `/system/bin/sh` 执行命令.

- `AziInit`

  初始化功能 (后台线程运行).

- `AziAsset`

  从 `apk/assets` 中复制 (释放) 文件出来.

- `AziWebView`

  对 Android WebView 的封装.

  TODO 默认提供 js bridge API: `azi_api`

  - `AziWebView.getWebView() -> WebView`

    返回内部的原始 WebView (用于自定义操作).

  - `AziWebView.addJsApi(name, api)`

    添加 JavascriptInterface

  - `AziWebView.loadUrl(url)`

    加载任意 URL

  - `AziWebView.loadLoader()`

    加载 (内置的) Azi ui-loader

  - `AziWebView.loadLocal(path)`

    加载本地文件 `file:///`

  - `AziWebView.loadAsset(path)`

    加载 apk assets `file:///android_asset/`

  - `AziWebView.loadSdcard(azi_env, path)`

    加载 azi 环境变量中的文件: `AZI_ENV_SDCARD_DATA`, `AZI_ENV_SDCARD_CACHE`

  默认 WebView 启用的功能:

  - js
  - localStorage
  - sessionStorage
  - TODO client ?
  - TODO 返回按键处理

## AziWebView API

- `azi_api.getJsLoadList() -> [""]`

  获取 ui-loader 需要加载 (注入) 的自定义 js 文件 URL.

  通常可加载 `file://AZI_DIR_SDCARD_DATA/xxx/xx.js`

- `azi_api.checkInit() -> ""`

  检查初始化结果 (返回显示的字符串).

TODO
