import os

from fastapi import File, UploadFile

TREE_FOLDER_PATH = "data/trees"


def save_tree_photo(file):
    try:
        contents = file.file.read()
        file_path = os.path.join(TREE_FOLDER_PATH, file.filename)
        with open(file_path, 'wb') as f:
            f.write(contents)
    except Exception as ex:
        print(f"Error while saving file {str(ex)}")
        raise ex
    return file_path
