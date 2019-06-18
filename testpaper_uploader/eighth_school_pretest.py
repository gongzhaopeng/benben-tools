import json
import logging
import requests

logging.basicConfig(level=logging.INFO)

_X_AUTH_CODE = 'b3d0369a-06b3-4cd8-8000-786765e63e46'

_CLONE_COUNT = 40

origin_paper_id = '0e31e676-aca5-4523-9236-9ea962c03c82'
origin_paper_url = f'https://api.xueliceping.com/openeva/testpapers/{origin_paper_id}'
origin_paper_req = requests.get(origin_paper_url, headers={
    'Accept': 'application/json',
    'X-Auth-Code': _X_AUTH_CODE
})
origin_paper = origin_paper_req.json()

with open(f'testpaper/pretest/origin_testpaper.json', 'w') as outfile:
    json.dump(origin_paper, outfile, ensure_ascii=False, indent=4)

clone_paper_ids = [
    "c85f040d-422c-44d1-aa42-ce5925ce465e",  # 1
    "9572161e-61b2-4fd0-bb62-15d7f6e8bba2",  # 2
    "088f08e5-9f09-405a-939f-16467a5250ac",  # 3
    "a3c37d32-a950-4c0e-822f-a2adf3f69732",  # 4
    "2bf34760-9461-4698-8503-e342d41b5fc8",  # 5
    "9d584d3b-71ed-4dd2-965d-0b86bb77e866",  # 6
    "98cf834f-2464-4fa9-967d-88a807d4b90f",  # 7
    "f2f1cb8e-167b-4222-9e0e-5995d0e8ed9e",  # 8
    "de781ed4-51d9-46e4-a0e8-97905bffca16",  # 9
    "c8548a64-42c7-4392-ad83-ddefbca7c114",  # 10
    "45245f61-e085-41fd-9cc1-85baab07efe7",  # 11
    "8040ff8f-bd24-4a2f-92bd-444ae4c4dfe5",  # 12
    "b49612df-011a-4869-a40f-d9a47a3bd1fb",  # 13
    "27f2980b-cb84-4c25-935b-3bfec158e25a",  # 14
    "792c3afe-b7c2-4273-b4c3-fbe160ef3a63",  # 15
    "8246a29e-62a5-48e7-8a6a-805deefebd54",  # 16
    "8ff04f14-63de-4f5a-a7ab-d4b7b521b0ad",  # 17
    "105826de-1218-4064-b7f9-d98b02afa455",  # 18
    "a5d86c44-c004-4df9-b8af-d2060bda9571",  # 19
    "0950f878-0436-4193-93d6-02e2b340da46",  # 20
    "5d1dba81-f1af-414c-a6e3-80fd818970aa",  # 21
    "8bdcd695-4acf-49e2-99d2-a15f020c800f",  # 22
    "30850fda-43cb-4012-adf2-36e042749ae7",  # 23
    "b5e29b67-b8db-4b64-a7aa-628127d6d193",  # 24
    "51ae94e3-bf10-49c8-995d-e2df47b3fce3",  # 25
    "c6dd8dc6-e1b3-4fca-947f-51748ce376ed",  # 26
    "7a33038f-9872-4b8d-bb95-b5a7a07c5c1d",  # 27
    "3dd00a83-f7d2-483f-865d-97ec4f916d86",  # 28
    "2505e397-2246-403a-bb3e-1bfa5d9bfed2",  # 29
    "39581dee-1cc3-45ec-82d7-dbaca22b5beb",  # 30
    "85bebcbd-7596-4c9a-a76f-6ef3ea88f2e8",  # 31
    "db2dea44-0edb-4180-9229-f63a99a522df",  # 32
    "2e03648d-769e-430f-b7cf-0cd50a6608b4",  # 33
    "3f9e4238-5d84-49e7-b861-4d7ef89bba2e",  # 34
    "612baf9d-c633-4e52-984d-2ab37e47b274",  # 35
    "e41695a3-4e2a-46d4-ade3-111c98d1e996",  # 36
    "b8f3e777-189f-492b-b7bc-912b2297f851",  # 37
    "11e6f527-e73a-4727-a6e4-fc5c096cc299",  # 38
    "f8a43361-c89f-4f62-ac78-d73ee82d514e",  # 39
    "70586170-70bc-47a5-8191-0036c4bf069e"  # 40
]

assert len(clone_paper_ids) == _CLONE_COUNT

for i, pid in enumerate(clone_paper_ids):
    description = f'八中体验试卷0618({i + 1})'
    cloned_paper = dict(origin_paper)
    cloned_paper['id'] = pid
    cloned_paper['description'] = description
    paper_update_url = f'https://api.xueliceping.com/openeva/testpapers/{pid}'
    paper_update_req = requests.put(paper_update_url, data=json.dumps(cloned_paper), headers={
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Auth-Code': _X_AUTH_CODE
    })

    with open(f'testpaper/pretest/{description}.json', 'w') as outfile:
        json.dump(paper_update_req.json(), outfile, ensure_ascii=False, indent=4)
