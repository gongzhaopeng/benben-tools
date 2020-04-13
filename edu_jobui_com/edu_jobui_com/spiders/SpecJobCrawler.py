import scrapy
import re


class SpecJobCrawler(scrapy.Spider):
    name = "spec_job"

    _EDU_LEVEL_BENKE = 'benke'
    _EDU_LEVEL_ZHUANKE = 'zhuanke'

    def __entry_url(self, edu_level):
        if edu_level == self._EDU_LEVEL_BENKE:
            return 'https://edu.jobui.com/majors/benke/'
        elif edu_level == self._EDU_LEVEL_ZHUANKE:
            return 'https://edu.jobui.com/majors/zhuanke/'
        else:
            raise ValueError(f'Unknown edu level: {edu_level}')

    def start_requests(self):
        entry_url = self.__entry_url(self.edu_level)

        yield scrapy.Request(url=entry_url, callback=self.parse)

    def parse(self, response):
        if '/majors/' in response.url:
            spec_item_urls = response.css('body > div.astruct > div.astruct > div.cfix > div.aleft > '
                                          'div.jk-matter > ul > li > ul > li a::attr(href)').getall()
            for spec_item_url in spec_item_urls:
                yield response.follow(spec_item_url, callback=self.parse)
        elif '/major/' in response.url:
            spec_title = ''
            try:
                spec_title = response.css('body > div.astruct > div.trends-head-chunk > div.trends-head-box > '
                                          'div.trends-head-information h1::text').get().strip()[:-2]
            except Exception:
                pass

            aleft_div = response.css('body > div.astruct > div.aleft')

            dist_dives = []
            try:
                dist_dives = list(aleft_div.css('div.aleft > div.modBar > div'))
            except Exception:
                pass

            industry_dist = []
            try:
                for industry_dist_item_li in dist_dives[0].css('ol > li'):
                    rank = int(industry_dist_item_li.css('em::text').get().strip())
                    industry = industry_dist_item_li.css('div > span > span > a::text').get().strip()
                    count = re.findall(r'\d+',
                                       ''.join(industry_dist_item_li.css('div > span > '
                                                                         'span > span *::text').getall()).strip()
                                       )[0]
                    percent = industry_dist_item_li.css('div > em::text').get().strip()
                    industry_dist.append({
                        'rank': rank,
                        'industry': industry,
                        'count': count,
                        'percent': percent
                    })
            except:
                pass

            area_dist = []
            try:
                for area_dist_item_li in dist_dives[1].css('ol > li'):
                    rank = int(area_dist_item_li.css('em::text').get().strip())
                    area = area_dist_item_li.css('div > span > span > a::text').get().strip()
                    count = re.findall(r'\d+',
                                       ''.join(
                                           area_dist_item_li.css('div > span > span > span *::text').getall()).strip()
                                       )[0]
                    percent = area_dist_item_li.css('div > em::text').get().strip()
                    area_dist.append({
                        'rank': rank,
                        'area': area,
                        'count': count,
                        'percent': percent
                    })
            except:
                pass

            acq_stat_dives = []
            try:
                acq_stat_dives = list(aleft_div.css('div.aleft > div.jk-box > div.cfix > div.hori3p1'))
            except Exception:
                pass

            salary_acq = []
            try:
                for salary_acq_stat_item_li in acq_stat_dives[0].css('ul > li'):
                    condition = salary_acq_stat_item_li.css('dfn::text').get().strip()
                    percent = salary_acq_stat_item_li.css('li > em::text').get().strip()
                    salary_acq.append({
                        'condition': condition,
                        'percent': percent
                    })
            except:
                pass

            experience_acq = []
            try:
                for experience_acq_stat_item_li in acq_stat_dives[1].css('ul > li'):
                    condition = experience_acq_stat_item_li.css('dfn::text').get().strip()
                    percent = experience_acq_stat_item_li.css('li > em::text').get().strip()
                    experience_acq.append({
                        'condition': condition,
                        'percent': percent
                    })
            except:
                pass

            edu_level_acq = []
            try:
                for edu_level_acq_stat_item_li in acq_stat_dives[2].css('ul > li'):
                    condition = edu_level_acq_stat_item_li.css('dfn::text').get().strip()
                    percent = edu_level_acq_stat_item_li.css('li > em::text').get().strip()
                    edu_level_acq.append({
                        'condition': condition,
                        'percent': percent
                    })
            except:
                pass

            spec_item = {
                'url': response.url,
                'eduLevel': self.edu_level,
                'title': spec_title,
                'industryDist': industry_dist,
                'areaDist': area_dist,
                'salaryAcq': salary_acq,
                'experienceAcq': experience_acq,
                'eduLevelAcq': edu_level_acq
            }

            yield spec_item
        else:
            pass
