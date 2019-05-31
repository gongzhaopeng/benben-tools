import scrapy


class SchoolCrawler(scrapy.Spider):
    name = 'school'

    def start_requests(self):
        def generate_url(area_code):
            base_url = \
                "https://gaokao.koolearn.com/college/school/" \
                    f"{area_code}/0/0/0/want"
            return base_url

        _AREA_CODE = ['3', '17', '28', '1', '24', '19', '2', '22', '9', '7', '8', '15', '16', '27',
                      '11', '10', '5', '6', '12', '29', '32', '13', '14', '31', '30', '26', '18',
                      '25', '4', '20', '21', '23']
        _TYPE_CODE = list(range(1, 14))
        _FEATURE_CODE = [9, 7, 8, 5, 4]
        _LEVEL_CODE = [1, 2, 3, 6, -1]

        for ac in _AREA_CODE:
            req_url = generate_url(ac)

            yield scrapy.Request(url=req_url, callback=self.parse)

    def parse(self, response):
        p20 = response.css('div.wrap div#content div.mt20 div.fl div.p20')

        try:
            schools = p20.css('ul li.fc div.w3 div.fc p.w4 a::text').getall()
            schools = [s.strip() for s in schools]
            for school in schools:
                yield {
                    '_id': school,
                    'type': 'school'
                }
        except Exception:
            pass

        try:
            pages = list(p20.css('div.page a'))
            next_page = [p.css('::attr(href)').get() for p in pages if p.css('::text').get().strip() == '下一页'][0]
            yield response.follow(url=next_page, callback=self.parse)
        except Exception:
            pass
