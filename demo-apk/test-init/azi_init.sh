#!/system/bin/sh
# azi demo init

echo "Azi Demo azi_init.sh test"

echo env AZI_DIR_APP_DATA=${AZI_DIR_APP_DATA}
echo env AZI_DIR_SDCARD_DATA=${AZI_DIR_SDCARD_DATA}
echo env AZI_DIR_SDCARD_CACHE=${AZI_DIR_SDCARD_CACHE}

# test AZI_DIR_APP_DATA
echo test write AZI_DIR_APP_DATA/test_app_data.txt

echo test_app_data 1 > ${AZI_DIR_APP_DATA}/test_app_data.txt
find ${AZI_DIR_APP_DATA}

# test AZI_DIR_SDCARD_DATA
echo test write AZI_DIR_SDCARD_DATA/test_sdcard_data.txt

echo test_sdcard_data 2 > ${AZI_DIR_SDCARD_DATA}/test_sdcard_data.txt
find ${AZI_DIR_SDCARD_DATA}

# test AZI_DIR_SDCARD_CACHE
echo test write AZI_DIR_SDCARD_CACHE/test_sdcard_cache.txt

echo test_sdcard_cache 3 > ${AZI_DIR_SDCARD_CACHE}/test_sdcard_cache.txt
find ${AZI_DIR_SDCARD_CACHE}

# test env
echo test env
env

echo test encode "测试中文编码, 喵喵"
# ok
echo test ok
