import json
import logging
import requests

logging.basicConfig(level=logging.INFO)

_X_AUTH_CODE = 'b3d0369a-06b3-4cd8-8000-786765e63e46'

_CLONE_COUNT = 1

origin_paper_id = '3824b471-030a-4f5c-8118-3b20bb276b2d'
origin_paper_url = f'https://api.xueliceping.com/openeva/testpapers/{origin_paper_id}'
origin_paper_req = requests.get(origin_paper_url, headers={
    'Accept': 'application/json',
    'X-Auth-Code': _X_AUTH_CODE
})
origin_paper = origin_paper_req.json()

with open(f'testpaper/pretest/origin_testpaper.json', 'w') as outfile:
    json.dump(origin_paper, outfile, indent=4)

for i in range(1, _CLONE_COUNT + 1):
    description = f'八中体验试卷0603({i})'
    cloned_paper = dict(origin_paper)
    cloned_paper['id'] = '00000000-0000-0000-0000-000000000000'
    cloned_paper['description'] = description
    paper_upload_url = 'https://api.xueliceping.com/openeva/testpapers'
    paper_upload_req = requests.post(paper_upload_url, data=json.dumps(cloned_paper), headers={
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Auth-Code': _X_AUTH_CODE
    })

    with open(f'testpaper/pretest/{description}.json', 'w') as outfile:
        json.dump(paper_upload_req.json(), outfile, indent=4)
