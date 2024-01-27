import requests
import math
from PIL import Image
from io import StringIO

def get_satellite_image_by_bbox(bobox):
    payload = {'l': 'sat', 'bbox': '{},{}~{},{}'.format(bobox[0], bobox[1], bobox[2], bobox[3]), 'size':'450,450'}
    r = requests.get('https://static-maps.yandex.ru/1.x/', params=payload)
    print('r.url', r.url)
    with open("response.jpg", "wb") as f:
        f.write(r.content)
    return "response.jpg"





def calculate_corner_coordinates(center_lat, center_lon, zoom, map_width, map_height):
    C = 256
    R = 6378137
    initial_resolution = 2 * math.pi * R / C

    resolution = initial_resolution / (2**(zoom+1))

    ground_width = map_width * resolution
    ground_height = map_height * resolution

    lat_rad = math.radians(center_lat)
    lat_coef = R * math.cos(lat_rad)

    lon_deg_per_pixel = ground_width / lat_coef
    lat_deg_per_pixel = ground_height / R

    top_lat = center_lat + (lat_deg_per_pixel / 2.0) * (180 / math.pi)
    bottom_lat = center_lat - (lat_deg_per_pixel / 2.0) * (180 / math.pi)
    left_lon = center_lon - (lon_deg_per_pixel / 2.0) * (180 / math.pi)
    right_lon = center_lon + (lon_deg_per_pixel / 2.0) * (180 / math.pi)

    return (top_lat, left_lon), (bottom_lat, right_lon)

# Example Usage
# center = (41.3160967, 69.2955408)
# zoom = 18
# image_size = (450, 450)  # width, height in pixels
# top_left, bottom_right = calculate_corner_coordinates(center[0], center[1], zoom, *image_size)
# print("Top Left:", top_left)
# print("Bottom Right:", bottom_right)
#


def pixel_to_coords(top_left:tuple, bottom_right:tuple, resolution:tuple, pixel: tuple) -> tuple:
    scale_x = (bottom_right[0] - top_left[0]) / resolution[0]
    scale_y = (bottom_right[1] - top_left[1]) / resolution[1]

    real_x = top_left[0] + (pixel[0] * scale_x)
    real_y = top_left[1] + (pixel[1] * scale_y)

    return real_x, real_y

def get_satellite_image_by_centre_and_zoom(center, zoom):
    payload = {'l': 'sat', 'll': '{},{}'.format(center[0], center[1]), 'size': '450,450', 'z': zoom}
    r = requests.get('https://static-maps.yandex.ru/1.x/', params=payload)
    print('r.url', r.url)
    top_left, bottom_right = calculate_corner_coordinates(center[0], center[1], zoom, 450, 450)
    with open("response.jpg", "wb") as f:
        f.write(r.content)
    return "response.jpg", top_left, bottom_right


if __name__ == '__main__':
    #bottom_right = (69.29714781413111, 41.3148897059433)
    center = (69.29634430706555, 41.31549320297165)
    #get_satellite_image_by_centre_and_zoom(centre=(69.2955408, 41.3160967), zoom=18)
    img, top_left, bottom_right = get_satellite_image_by_centre_and_zoom(center=center, zoom=18)
    print('top_left', top_left)
    print('bottom_right', bottom_right)
    real_x, real_y = pixel_to_coords(top_left, bottom_right, (450, 450), (234, 290))
    print('real_x, real_y', real_x, real_y)
    #(41.18, 69.15)