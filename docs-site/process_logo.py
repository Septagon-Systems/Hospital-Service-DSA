import os
from PIL import Image
import numpy as np

src_path = '/run/media/ahorts/KAGE/Users/Godways Ahorbor/Downloads/Septagon/septagon_upscayl_4x_upscayl-standard-4x.png'
dest_dir = '/home/ahorts/Documents/Projects/308/Hospital-Service-DSA/docs-site/public'

os.makedirs(dest_dir, exist_ok=True)

if os.path.exists(src_path):
    img = Image.open(src_path).convert('RGBA')
    arr = np.array(img, dtype=np.float32)

    r, g, b, a = arr[:, :, 0], arr[:, :, 1], arr[:, :, 2], arr[:, :, 3]

    # Brightness threshold for black background removal
    brightness = np.maximum(r, np.maximum(g, b))

    # Mask out background (< 28 rgb)
    bg_mask = brightness < 28.0

    # Smooth anti-aliased transparency channel
    alpha = np.where(bg_mask, 0.0, np.minimum(255.0, (brightness - 12.0) * 12.0))

    arr_transparent = arr.copy()
    arr_transparent[:, :, 3] = alpha
    img_transparent = Image.fromarray(arr_transparent.astype(np.uint8))

    # Crop tightly around content bounding box
    bbox = img_transparent.getbbox()
    if bbox:
        # Add a tiny 4px padding so edges aren't clipped
        pad = 4
        bbox = (
            max(0, bbox[0] - pad),
            max(0, bbox[1] - pad),
            min(img_transparent.width, bbox[2] + pad),
            min(img_transparent.height, bbox[3] + pad)
        )
        img_transparent = img_transparent.crop(bbox)

    # Save transparent cropped logo for dark mode
    img_transparent.save(os.path.join(dest_dir, 'logo-dark.png'))
    img_transparent.save(os.path.join(dest_dir, 'logo.png'))

    # Light Mode Logo (Invert white text to dark slate #0F172A while preserving warm copper emblem)
    arr_cropped = np.array(img_transparent, dtype=np.float32)
    r_chan, g_chan, b_chan = arr_cropped[:, :, 0], arr_cropped[:, :, 1], arr_cropped[:, :, 2]

    # White text mask
    is_white_text = (r_chan > 160) & (g_chan > 160) & (b_chan > 160) & (abs(r_chan - g_chan) < 30) & (abs(r_chan - b_chan) < 30)

    # Shift white text pixels to dark slate #0F172A (15, 23, 42)
    arr_cropped[:, :, 0] = np.where(is_white_text, 15.0, r_chan)
    arr_cropped[:, :, 1] = np.where(is_white_text, 23.0, g_chan)
    arr_cropped[:, :, 2] = np.where(is_white_text, 42.0, b_chan)

    img_light = Image.fromarray(arr_cropped.astype(np.uint8))
    img_light.save(os.path.join(dest_dir, 'logo-light.png'))

    print(f'Processed & tightly cropped high-res logo: new size {img_transparent.size}!')
else:
    print(f'Source logo not found at {src_path}')
