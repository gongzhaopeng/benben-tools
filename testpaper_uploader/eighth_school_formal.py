import json
import logging
import requests

logging.basicConfig(level=logging.INFO)

_X_AUTH_CODE = 'b3d0369a-06b3-4cd8-8000-786765e63e46'

_FHYS_PAPER_IDS = [
    "b4b929c2-af61-4a40-a242-37d8fae69e26",  # 0610_A1
    "428f20d8-0f39-4db3-8246-36749d270baa",  # 0610_A2
    "4f8089f4-5330-48ae-92bf-0453aa6de0b0",  # 0610_A3
    "fa25f325-13f0-4848-997c-b2ffebafd0ab",  # 0610_A4
    "c8d97492-06c3-4ae4-bf77-753be48de599",  # 0610_A5

    "c8e3ecb4-33f0-4ecc-9752-3da8ae8983d0",  # 0610_B1
    "37ac4a78-bacb-41b7-9b0a-c9b35f661c96",  # 0610_B2
    "9a87cc7f-e447-442b-a383-a8e62b5b1554",  # 0610_B3
    "a5f3be8a-c8c2-4b80-85ab-af4acd8c71a5",  # 0610_B4
    "c66606b3-cbc2-494b-bc2c-be3f1ab190f9",  # 0610_B5
]

_TXTL_PAPER_IDS = [
    "c5afdd98-7ad1-4825-9a6e-d5e74b1092ab",  # 0610_A1
    "6284dd0c-3ccd-4452-a402-2b5d8e7ed7d7",  # 0610_B1
]

_ZKT_PAPER_IDS = [
    "c0bf7d3e-ce30-4353-b7ef-b2457824bb64",  # 0610_A
    "0d636380-a6dc-4f24-9bc0-d788c03c238b",  # 0610_B
]

origin_paper_base_url = 'https://api.xueliceping.com/openeva/testpapers/'
headers = {
    'Accept': 'application/json',
    'X-Auth-Code': _X_AUTH_CODE
}

fhys_papers = []
for id in _FHYS_PAPER_IDS:
    url = origin_paper_base_url + id
    fhys_papers.append(requests.get(url, headers=headers).json())

txtl_papers = []
for id in _TXTL_PAPER_IDS:
    url = origin_paper_base_url + id
    txtl_papers.append(requests.get(url, headers=headers).json())

zkt_papers = []
for id in _ZKT_PAPER_IDS:
    url = origin_paper_base_url + id
    zkt_papers.append(requests.get(url, headers=headers).json())

formal_paper_template = dict(fhys_papers[0])
formal_paper_template['id'] = '00000000-0000-0000-0000-000000000000'
formal_paper_template['description'] = ''
formal_paper_template['question_sets'] = []

formal_papers = []
for fhys_paper in fhys_papers:
    for txtl_paper in txtl_papers:
        for zkt_paper in zkt_papers:
            formal_paper = dict(formal_paper_template)
            formal_paper['question_sets'] = []

            formal_paper['description'] = f"八中正式题_0611_{fhys_paper['description'][-2:]}" \
                f"{txtl_paper['description'][-2:]}{zkt_paper['description'][-1:]}"
            formal_paper['question_sets'].append(fhys_paper['question_sets'][0])
            formal_paper['question_sets'].append(txtl_paper['question_sets'][0])
            formal_paper['question_sets'].append(zkt_paper['question_sets'][0])

            formal_papers.append(formal_paper)

# for formal_paper in formal_papers:
#     paper_upload_url = 'https://api.xueliceping.com/openeva/testpapers'
#     paper_upload_req = requests.post(paper_upload_url, data=json.dumps(formal_paper), headers={
#         'Content-Type': 'application/json',
#         'Accept': 'application/json',
#         'X-Auth-Code': _X_AUTH_CODE
#     })
#
#     with open(f'testpaper/formal/{formal_paper["description"]}.json', 'w') as outfile:
#         json.dump(paper_upload_req.json(), outfile, ensure_ascii=False, indent=4)

logging.info("Finish...")
