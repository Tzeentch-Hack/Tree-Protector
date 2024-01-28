import os

import numpy as np
from PIL import Image
from torchvision import transforms as T
from transformers import MaskFormerForInstanceSegmentation, MaskFormerImageProcessor
from scipy.ndimage import binary_erosion, binary_dilation
from typing import Tuple
from dotenv import load_dotenv

load_dotenv()


class TreeSegmenter:
    def __init__(self):
        self.ade_mean = [0.485, 0.456, 0.406]
        self.ade_std = [0.229, 0.224, 0.225]

        self.test_transform = T.Compose([
            T.ToTensor(),
            T.Normalize(mean=self.ade_mean, std=self.ade_std)
        ])

        self.palette = [
            [120, 120, 120], [4, 200, 4], [4, 4, 250], [6, 230, 230],
            [80, 50, 50], [120, 120, 80], [140, 140, 140], [204, 5, 255]
        ]

        self.model_id = f"thiagohersan/maskformer-satellite-trees"
        self.labels = ["vegetation"]

        # preprocessor = MaskFormerImageProcessor.from_pretrained(model_id)
        self.preprocessor = MaskFormerImageProcessor(
            do_resize=False,
            do_normalize=False,
            do_rescale=False,
            ignore_index=255,
            reduce_labels=False
        )

        hf_token = os.environ.get('HFTOKEN')

        self.model = MaskFormerForInstanceSegmentation.from_pretrained(self.model_id, use_auth_token=hf_token)

    def visualize_instance_seg_mask(self, img_in, mask, id2label, included_labels):
        img_out_colored = np.zeros((mask.shape[0], mask.shape[1], 3))
        img_out_binary = np.zeros((mask.shape[0], mask.shape[1], 3))
        image_total_pixels = mask.shape[0] * mask.shape[1]
        label_ids = np.unique(mask)

        id2color = {id: self.palette[id] for id in label_ids}
        id2count = {id: 0 for id in label_ids}

        for i in range(img_out_colored.shape[0]):
            for j in range(img_out_colored.shape[1]):
                img_out_colored[i, j, :] = id2color[mask[i, j]]
                img_out_binary[i, j, :] = (255, 255, 255) if mask[i, j] == 1 else (0, 0, 0)
                id2count[mask[i, j]] = id2count[mask[i, j]] + 1

        image_res = (0.5 * img_in + 0.5 * img_out_colored).astype(np.uint8)
        image_binary_res = img_out_binary.astype(np.uint8)
        dataframe = [[
            f"{id2label[id]}",
            f"{(100 * id2count[id] / image_total_pixels):.2f} %",
            f"{np.sqrt(id2count[id] / image_total_pixels):.2f} m"
        ] for id in label_ids if id2label[id] in included_labels]

        if len(dataframe) < 1:
            dataframe = [[
                f"",
                f"{(0):.2f} %",
                f"{(0):.2f} m"
            ]]
        percentage = float(dataframe[0][1][:-2])
        return image_res, image_binary_res, percentage

    def query_image(self, image_path:str) -> Tuple[np.ndarray, np.ndarray, float]:
        img = np.array(Image.open(image_path))
        img_size = (img.shape[0], img.shape[1])
        inputs = self.preprocessor(images=self.test_transform(img), return_tensors="pt")
        outputs = self.model(**inputs)
        results = self.preprocessor.post_process_semantic_segmentation(outputs=outputs, target_sizes=[img_size])[0]
        mask_img, mask_img_binary, percentage = self.visualize_instance_seg_mask(img, results.numpy(),
                                                                                 self.model.config.id2label,
                                                                                 self.labels)
        return mask_img, mask_img_binary, percentage

    def compare_masks(self, mask_prev: np.ndarray, mask_next: np.ndarray, noise_filter_size: int = 7) -> np.ndarray:
        """
        :return: Array, where each pixel will be either [0, 0, 0] if not changed
        or [0, 255, 0] if trees were added or [255, 0, 0] if trees were removed
        """
        if mask_prev.shape != mask_next.shape:
            raise ValueError("Masks should have the same dimensions")

        output = np.zeros_like(mask_prev)

        added = np.all(mask_next == [255, 255, 255], axis=-1) & np.all(mask_prev == [0, 0, 0], axis=-1)
        removed = np.all(mask_prev == [255, 255, 255], axis=-1) & np.all(mask_next == [0, 0, 0], axis=-1)

        structure = np.ones((noise_filter_size, noise_filter_size))
        added_filtered = binary_dilation(binary_erosion(added, structure=structure), structure=structure)
        removed_filtered = binary_dilation(binary_erosion(removed, structure=structure), structure=structure)

        output[added_filtered] = [0, 255, 0]
        output[removed_filtered] = [255, 0, 0]

        return output


if __name__ == "__main__":
    tree_segmenter = TreeSegmenter()
    import time
    input_name = "response_3.jpg"
    start_time = time.time()
    mask_img, mask_img_binary, percentage = tree_segmenter.query_image(f"../data/input/{input_name}")
    end_time = time.time()
    print(end_time - start_time)
    pil_image = Image.fromarray(mask_img)
    pil_image_binary = Image.fromarray(mask_img_binary)
    pil_image.save(f'../data/output/{input_name[:-4]}.jpg')
    pil_image_binary.save(f'../data/output/mask_binary_{input_name[:-4]}.jpg')
    print('dataframe:', percentage)
