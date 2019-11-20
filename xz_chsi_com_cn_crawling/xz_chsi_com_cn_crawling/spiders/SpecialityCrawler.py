import scrapy


class SpecialityCrawler(scrapy.Spider):
    name = "speciality"

    def start_requests(self):
        def generate_url(start):
            return f'https://xz.chsi.com.cn/speciality/list.action?start={start}'

        _TOTAL_PAGE = 57
        _PAGE_SIZE = 15

        for page_number in range(0, _TOTAL_PAGE):
            req_url = generate_url(page_number * _PAGE_SIZE)

            yield scrapy.Request(url=req_url, callback=self.parse)

    def parse(self, response):
        if 'list.action' in response.url:
            spec_urls = response.css('table tbody tr td a::attr(href)').getall()
            for spec_url in spec_urls:
                yield response.follow(spec_url, callback=self.parse)
        elif 'detail.action' in response.url:
            content_div = response.css('div.body div.width1000')

            try:
                title = content_div.css('div.titleh3 h3::text').get().strip()
            except Exception:
                title = ''

            try:
                basic_infos = list(content_div.css('div.warp-jbxx ul.jbxx-list li::text').getall())

                edu_level = basic_infos[-1].strip()
            except Exception:
                edu_level = ''

            try:
                definition = content_div.css('p.zymsg::text').get().strip()
            except Exception:
                definition = ''

            try:
                master_source_speces = content_div.css('div.warp-jbxx p.margintop20 a::text').getall()
            except Exception:
                master_source_speces = []

            satisfactions = []
            try:
                sat_trs = content_div.css('div.clearfix div.zymydpj table tr')
                for sat_tr in sat_trs:
                    try:
                        sat_tds = list(sat_tr.css('td'))

                        satisfactions.append({
                            'title': sat_tds[0].css('::text').get().strip()[:-1],
                            'score': float(sat_tds[1].css('span.zymydpj-score ::text').get().strip()),
                            'count': int(sat_tds[2].css('span ::text').getall()[1].strip()[:-2])
                        })
                    except Exception:
                        pass
            except Exception:
                pass

            try:
                emp_div = content_div.css('div.warp-cyfx div#div_list')

                emp_directions = emp_div.css('div#occ ul li a::text').getall()
                exp_emp_directions = emp_div.css('div#exp_occ ul li a::text').getall()
            except Exception:
                emp_directions = []
                exp_emp_directions = []

            try:
                graduate_scale = content_div.css('div.warp-tjxx div.tjxx-map-first div::text').get()[:-1].split('-')
            except Exception:
                graduate_scale = [0, 0]

            spec_item = {
                'title': title,
                'eduLevel': edu_level,
                'definition': definition,
                'masterSourceSpeces': master_source_speces,
                'satisfactions': satisfactions,
                'empDirections': emp_directions,
                'expEmpDirections': exp_emp_directions,
                'graduateScale': graduate_scale,
                'url': response.url
            }

            yield spec_item
        else:
            pass
