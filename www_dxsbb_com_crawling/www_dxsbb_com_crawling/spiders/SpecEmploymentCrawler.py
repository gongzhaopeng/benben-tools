import scrapy


class SpecEmploymentCrawler(scrapy.Spider):
    name = "spec_employment"

    def start_requests(self):
        def generate_url(p_number):
            return f'https://m.dxsbb.com/news/list_90_{p_number}.html'

        _TOTAL_PAGE = 17
        _FIRST_PAGE_URL = 'https://m.dxsbb.com/news/list_90.html'

        yield scrapy.Request(url=_FIRST_PAGE_URL, callback=self.parse)
        for page_number in range(2, _TOTAL_PAGE + 1):
            req_url = generate_url(page_number)

            yield scrapy.Request(url=req_url, callback=self.parse)

    def parse(self, response):
        if 'list_90' in response.url:
            spec_item_urls = \
                response.css('body section div.newslist div.pageBox dl dd.listBox2 ul li a::attr(href)').getall()
            for spec_item_url in spec_item_urls:
                yield response.follow(spec_item_url, callback=self.parse)
        else:
            contents = []
            try:
                content_segments = response.css('article.contentbox div#content > *')
                for content_segment in content_segments:
                    try:
                        raw_content = content_segment.css('*::text').getall()
                        content = ''.join(raw_content).strip()
                        if content:
                            contents.append(content)
                    except Exception:
                        pass
            except Exception:
                pass

            spec_title = ''
            try:
                spec_title = response.css('div.mark_share div.mark > a::text').get().strip()
            except Exception:
                pass

            spec_item = {
                'url': response.url,
                'title': spec_title,
                'contents': contents
            }

            yield spec_item
