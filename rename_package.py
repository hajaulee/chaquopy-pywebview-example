"""Main."""
import sys
import json
import os
from glob import glob
import shutil

def rename(package_name: str, app_name: str):
    # Change package path
    root_paths = ["android.app.src.main.java", "android.app.src.androidTest.java", "android.app.src.test.java"]
    old_package = "com.chaquopy.example"
    old_module = "com.chaquopy"
    for root_path in root_paths:
        os.makedirs(os.path.join(*root_path.split("."), *package_name.split(".")), exist_ok=True)
        for file in os.listdir(os.path.join(*root_path.split("."), *old_package.split("."))):
            shutil.move(
                os.path.join(*root_path.split("."), *old_package.split("."), file), 
                os.path.join(*root_path.split("."), *package_name.split("."))
            )
        if not package_name.startswith(old_module):
            shutil.rmtree(os.path.join(*root_path.split("."), *old_module.split(".")))

    # Change package info in file contents
    for filename in glob('android/**', recursive=True):
        if os.path.isfile(filename) and os.path.splitext(filename)[1] not in [".png", ".jpg", ".jar"]:
            print(filename)
            try:
                with open(filename, "r") as file:
                    file_content = file.read()
            except:
                continue
            with open(filename, "w") as file:
                file_content = file_content.replace(old_package, package_name)
                file_content = file_content.replace("EXAMPLE_APP_NAME", app_name)
                file.write(file_content)


def main():
    """Main."""
    if len(sys.argv) != 3:
        print("""Example: python rename_package.py com.example.hello "Hello world" """)
        exit(-1)
    rename(sys.argv[1], sys.argv[2])

if __name__ == "__main__":
    main()