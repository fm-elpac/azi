#![deny(unsafe_code)]

use std::env;
use std::fs::{self, File};
use std::io;
use std::path::Path;
use std::process::exit;

use zip::ZipArchive;

fn main() {
    // 命令行参数: zip, target_dir
    let a: Vec<_> = env::args().collect();
    if a.len() < 2 {
        // 错误的命令行参数
        exit(1);
    }

    // 输入 zip 文件
    let i_zip = Path::new(&a[1]);
    // 输出目录
    let o_dir = Path::new(&a[2]);

    // 读取 zip 文件
    let zip_file = File::open(i_zip).unwrap();
    let mut a = ZipArchive::new(zip_file).unwrap();
    // 解压每个文件
    for i in 0..a.len() {
        let mut f = a.by_index(i).unwrap();
        let outpath = match f.enclosed_name() {
            Some(path) => path,
            None => continue,
        };

        // 拼接目标路径
        let o = o_dir.join(outpath);
        // DEBUG
        println!("{}", o.to_string_lossy());

        if f.is_dir() {
            // 创建目录
            fs::create_dir_all(&o).unwrap();
        } else {
            // 创建上级目录 (如果不存在)
            if let Some(p) = o.parent() {
                if !p.exists() {
                    fs::create_dir_all(p).unwrap();
                }
            }
            // 解压文件
            let mut o_f = File::create(&o).unwrap();
            io::copy(&mut f, &mut o_f).unwrap();
        }
    }

    // 完成
    exit(0);
}
