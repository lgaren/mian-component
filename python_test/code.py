# #!/usr/bin/env python
# # -*- coding: utf-8 -*-
#
# """
# Created on 2017/11/102017 十一月 星期五下午 15:12
#
# mian-component
#
# @author: DSG
# """
# code = """
#    system.registerGenericUDF("concat");
#     system.registerUDF("substr", false);
#     system.registerUDF("substring", false);
#     system.registerGenericUDF("substring_index");
#     system.registerUDF("space", false);
#     system.registerUDF("repeat", false);
#     system.registerUDF("ascii", false);
#     system.registerGenericUDF("lpad" );
#     system.registerGenericUDF("rpad");
#     system.registerGenericUDF("levenshtein");
#     system.registerGenericUDF("soundex");
#
#     system.registerGenericUDF("size");
#
#     system.registerGenericUDF("round");
#     system.registerGenericUDF("bround");
#     system.registerGenericUDF("floor");
#     system.registerUDF("sqrt", false);
#     system.registerGenericUDF("cbrt");
#     system.registerGenericUDF("ceil");
#     system.registerGenericUDF("ceiling");
#     system.registerUDF("rand", false);
#     system.registerGenericUDF("abs");
#     system.registerGenericUDF("sq_count_check");
#     system.registerGenericUDF("pmod");
#
#     system.registerUDF("ln", false);
#     system.registerUDF("log2", false);
#     system.registerUDF("sin", false);
#     system.registerUDF("asin", false);
#     system.registerUDF("cos", false);
#     system.registerUDF("acos", false);
#     system.registerUDF("log10", false);
#     system.registerUDF("log", false);
#     system.registerUDF("exp", false);
#     system.registerGenericUDF("power");
#     system.registerGenericUDF("pow");
#     system.registerUDF("sign", false);
#     system.registerUDF("pi", false);
#     system.registerUDF("degrees", false);
#     system.registerUDF("radians", false);
#     system.registerUDF("atan", false);
#     system.registerUDF("tan", false);
#     system.registerUDF("e", false);
#     system.registerGenericUDF("factorial");
#     system.registerUDF("crc32", false);
#
#     system.registerUDF("conv", false);
#     system.registerUDF("bin", false);
#     system.registerUDF("chr", false);
#     system.registerUDF("hex", false);
#     system.registerUDF("unhex", false);
#     system.registerUDF("base64", false);
#     system.registerUDF("unbase64", false);
#     system.registerGenericUDF("sha2");
#     system.registerUDF("md5", false);
#     system.registerUDF("sha1", false);
#     system.registerUDF("sha", false);
#     system.registerGenericUDF("aes_encrypt");
#     system.registerGenericUDF("aes_decrypt");
#     system.registerUDF("uuid", false);
#
#     system.registerGenericUDF("encode");
#     system.registerGenericUDF("decode");
#
#     system.registerGenericUDF("upper");
#     system.registerGenericUDF("lower");
#     system.registerGenericUDF("ucase");
#     system.registerGenericUDF("lcase");
#     system.registerGenericUDF("trim");
#     system.registerGenericUDF("ltrim");
#     system.registerGenericUDF("rtrim");
#     system.registerGenericUDF("length");
#     system.registerGenericUDF("character_length");
#     system.registerGenericUDF("char_length");
#     system.registerGenericUDF("octet_length");
#     system.registerUDF("reverse", false);
#     system.registerGenericUDF("field");
#     system.registerUDF("find_in_set", false);
#     system.registerGenericUDF("initcap");
#
#     system.registerUDF("like", true);
#     system.registerGenericUDF("rlike");
#     system.registerGenericUDF("regexp");
#     system.registerUDF("regexp_replace", false);
#     system.registerUDF("replace", false);
#     system.registerUDF("regexp_extract", false);
#     system.registerUDF("parse_url", false);
#     system.registerGenericUDF("nvl");
#     system.registerGenericUDF("split");
#     system.registerGenericUDF("str_to_map");
#     system.registerGenericUDF("translate");
#
#     system.registerGenericUDF(UNARY_PLUS_FUNC_NAME);
#     system.registerGenericUDF(UNARY_MINUS_FUNC_NAME);
#
#     system.registerUDF("day", false);
#     system.registerUDF("dayofmonth", false);
#     system.registerUDF("dayofweek", false);
#     system.registerUDF("month", false);
#     system.registerGenericUDF("quarter");
#     system.registerUDF("year", false);
#     system.registerUDF("hour", false);
#     system.registerUDF("minute", false);
#     system.registerUDF("second", false);
#     system.registerUDF("from_unixtime", false);
#     system.registerGenericUDF("to_date");
#     system.registerUDF("weekofyear", false);
#     system.registerGenericUDF("last_day");
#     system.registerGenericUDF("next_day");
#     system.registerGenericUDF("trunc");
#     system.registerGenericUDF("date_format");
#
#     // Special date formatting functions
#     system.registerUDF("floor_year", false);
#     system.registerUDF("floor_quarter", false);
#     system.registerUDF("floor_month", false);
#     system.registerUDF("floor_day", false);
#     system.registerUDF("floor_week", false);
#     system.registerUDF("floor_hour", false);
#     system.registerUDF("floor_minute", false);
#     system.registerUDF("floor_second", false);
#
#     system.registerGenericUDF("date_add");
#     system.registerGenericUDF("date_sub");
#     system.registerGenericUDF("datediff");
#     system.registerGenericUDF("add_months");
#     system.registerGenericUDF("months_between");
#
#     system.registerUDF("get_json_object", false);
#
#     system.registerUDF("xpath_string", false);
#     system.registerUDF("xpath_boolean", false);
#     system.registerUDF("xpath_number", false);
#     system.registerUDF("xpath_double", false);
#     system.registerUDF("xpath_float", false);
#     system.registerUDF("xpath_long", false);
#     system.registerUDF("xpath_int", false);
#     system.registerUDF("xpath_short", false);
#     system.registerGenericUDF("xpath");
#
#     system.registerGenericUDF("+");
#     system.registerGenericUDF("-");
#     system.registerGenericUDF("*");
#     system.registerGenericUDF("/");
#     system.registerGenericUDF("%");
#     system.registerGenericUDF("mod");
#     system.registerUDF("div", true);
#
#     system.registerUDF("&", true);
#     system.registerUDF("|", true);
#     system.registerUDF("^", true);
#     system.registerUDF("~", true);
#     system.registerUDF("shiftleft", true);
#     system.registerUDF("shiftright", true);
#     system.registerUDF("shiftrightunsigned", true);
# """
# import re
# lines  = code.split("\n")
# for line in lines:
#     a = re.sub(r', true',"",line)
#     print  re.sub(r', false',"",a)
