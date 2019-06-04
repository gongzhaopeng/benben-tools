import json
import os

filenames = os.listdir('testpaper/pretest')
for filename in filenames:
    in_path = f'testpaper/pretest/{filename}'
    with open(in_path, 'r') as infile:
        data = json.load(infile)
        out_path = f'transformed/pretest/{filename}'
        with open(out_path, 'w') as outfile:
            json.dump(data, outfile, ensure_ascii=False, indent=4)
