import scrapy


class SchoolCrawler(scrapy.Spider):
    name = "school"

    def start_requests(self):
        def generate_url(province):
            return f'http://school.zhongkao.com/province/{province}/'

        _PROVINCE_DOMAIN = [
            1908,  # 河北
            # 1199,  # 重庆
            # 3822,  # 西藏
        ]

        for p in _PROVINCE_DOMAIN:
            req_url = generate_url(p)

            yield scrapy.Request(url=req_url, callback=self.parse)

    def parse(self, response):
        if response.url.find('province') >= 0:
            section = response.css('div.bgf div.filtresult section.w725')

            try:
                schools = list(section.css('article dl dt a::attr(href)').getall())
                for school in schools:
                    yield response.follow(school, callback=self.parse)
            except Exception:
                pass

            try:
                next_page = list(section.css('nav a'))[-1].css('::attr(href)').get()
                if next_page:
                    yield response.follow(next_page, callback=self.parse)
            except Exception:
                pass
        else:
            try:
                identity = '>'.join(response.css('nav.wrapper a::text').getall())
                items = list(response.css('div.bgf div.school article dl.clearfix dd table tr td'))
                detail = [''.join(item.css('*::text').getall()) for item in items]

                yield {
                    '_id': identity,
                    'detail': detail
                }
            except Exception:
                pass
