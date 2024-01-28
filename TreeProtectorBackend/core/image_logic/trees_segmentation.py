from PIL import Image

from ml.tree_segmenter import TreeSegmenter
from ml.gateway import get_satellite_image_by_centre_and_zoom

import os
from datetime import datetime

tree_segmentor = TreeSegmenter()


SATELLITE_IMAGE_PATH = "data/satellite"


def save_image(image_data, x, y, zoom, machine_id, image_type):
    try:
        image_name = f"{image_type}_{machine_id}_{x}_y{y}_{zoom}_{datetime.utcnow().strftime('%Y-%m-%d_%H:%M')}.jpg"
        image_path = os.path.join(SATELLITE_IMAGE_PATH, image_name)
        pil_image = Image.fromarray(image_data)
        pil_image.save(image_path)
    except Exception as ex:
        print(f"Error! {str(ex)}")
        raise ex
    return image_path


def make_tree_segmentation(x, y, zoom, machine_id):
    image_path, top_left, bottom_right = get_satellite_image_by_centre_and_zoom((x, y), zoom, machine_id)
    mask_img, mask_img_binary, percentage = tree_segmentor.query_image(image_path)
    colored_image_path = save_image(mask_img, x, y, zoom, machine_id, "colored_image")
    binary_image_path = save_image(mask_img_binary, x, y, zoom, machine_id, "binary_image")
    return colored_image_path, binary_image_path, percentage
