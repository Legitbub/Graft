import re

data = """AD	AND	20
United Arab Emirates Flag
United Arab Emirates	AE	ARE	784
Afghanistan Flag
Afghanistan	AF	AFG	4
Antigua and Barbuda Flag
Antigua and Barbuda	AG	ATG	28
Anguilla Flag
Anguilla	AI	AIA	660
Albania Flag
Albania	AL	ALB	8
Armenia Flag
Armenia	AM	ARM	51
Angola Flag
Angola	AO	AGO	24
Argentina Flag
Argentina	AR	ARG	32
American Samoa Flag
American Samoa	AS	ASM	16
Austria Flag
Austria	AT	AUT	40
Australia Flag
Australia	AU	AUS	36
Aruba Flag
Aruba	AW	ABW	533
Azerbaijan Flag
Azerbaijan	AZ	AZE	31
Bosnia and Herzegovina Flag
Bosnia and Herzegovina	BA	BIH	70
Barbados Flag
Barbados	BB	BRB	52
Bangladesh Flag
Bangladesh	BD	BGD	50
Belgium Flag
Belgium	BE	BEL	56
Burkina Faso Flag
Burkina Faso	BF	BFA	854
Bulgaria Flag
Bulgaria	BG	BGR	100
Bahrain Flag
Bahrain	BH	BHR	48
Burundi Flag
Burundi	BI	BDI	108
Benin Flag
Benin	BJ	BEN	204
Saint Barthelemy Flag
Saint Barthelemy	BL	BLM	652
Bermuda Flag
Bermuda	BM	BMU	60
Brunei Flag
Brunei	BN	BRN	96
Bolivia Flag
Bolivia	BO	BOL	68
Brazil Flag
Brazil	BR	BRA	76
Bahamas Flag
Bahamas	BS	BHS	44
Bhutan Flag
Bhutan	BT	BTN	64
Botswana Flag
Botswana	BW	BWA	72
Belarus Flag
Belarus	BY	BLR	112
Belize Flag
Belize	BZ	BLZ	84
Canada Flag
Canada	CA	CAN	124
DR Congo Flag
DR Congo	CD	COD	180
Central African Republic Flag
Central African Republic	CF	CAF	140
Republic of the Congo Flag
Republic of the Congo	CG	COG	178
Switzerland Flag
Switzerland	CH	CHE	756
Ivory Coast Flag
Ivory Coast	CI	CIV	384
Cook Islands Flag
Cook Islands	CK	COK	184
Chile Flag
Chile	CL	CHL	152
Cameroon Flag
Cameroon	CM	CMR	120
China Flag
China	CN	CHN	156
Colombia Flag
Colombia	CO	COL	170
Costa Rica Flag
Costa Rica	CR	CRI	188
Cuba Flag
Cuba	CU	CUB	192
Cape Verde Flag
Cape Verde	CV	CPV	132
Curacao Flag
Curacao	CW	CUW	531
Cyprus Flag
Cyprus	CY	CYP	196
Czechia Flag
Czechia	CZ	CZE	203
Germany Flag
Germany	DE	DEU	276
Djibouti Flag
Djibouti	DJ	DJI	262
Denmark Flag
Denmark	DK	DNK	208
Dominica Flag
Dominica	DM	DMA	212
Dominican Republic Flag
Dominican Republic	DO	DOM	214
Algeria Flag
Algeria	DZ	DZA	12
Ecuador Flag
Ecuador	EC	ECU	218
Estonia Flag
Estonia	EE	EST	233
Egypt Flag
Egypt	EG	EGY	818
Western Sahara Flag
Western Sahara	EH	ESH	732
Eritrea Flag
Eritrea	ER	ERI	232
Spain Flag
Spain	ES	ESP	724
Ethiopia Flag
Ethiopia	ET	ETH	231
Finland Flag
Finland	FI	FIN	246
Fiji Flag
Fiji	FJ	FJI	242
Falkland Islands Flag
Falkland Islands	FK	FLK	238
Micronesia Flag
Micronesia	FM	FSM	583
Faroe Islands Flag
Faroe Islands	FO	FRO	234
France Flag
France	FR	FRA	250
Gabon Flag
Gabon	GA	GAB	266
United Kingdom Flag
United Kingdom	GB	GBR	826
Grenada Flag
Grenada	GD	GRD	308
Georgia Flag
Georgia	GE	GEO	268
French Guiana Flag
French Guiana	GF	GUF	254
Guernsey Flag
Guernsey	GG	GGY	831
Ghana Flag
Ghana	GH	GHA	288
Gibraltar Flag
Gibraltar	GI	GIB	292
Greenland Flag
Greenland	GL	GRL	304
Gambia Flag
Gambia	GM	GMB	270
Guinea Flag
Guinea	GN	GIN	324
Guadeloupe Flag
Guadeloupe	GP	GLP	312
Equatorial Guinea Flag
Equatorial Guinea	GQ	GNQ	226
Greece Flag
Greece	GR	GRC	300
Guatemala Flag
Guatemala	GT	GTM	320
Guam Flag
Guam	GU	GUM	316
Guinea-Bissau Flag
Guinea-Bissau	GW	GNB	624
Guyana Flag
Guyana	GY	GUY	328
Hong Kong Flag
Hong Kong	HK	HKG	344
Honduras Flag
Honduras	HN	HND	340
Croatia Flag
Croatia	HR	HRV	191
Haiti Flag
Haiti	HT	HTI	332
Hungary Flag
Hungary	HU	HUN	348
Indonesia Flag
Indonesia	ID	IDN	360
Ireland Flag
Ireland	IE	IRL	372
Israel Flag
Israel	IL	ISR	376
Isle of Man Flag
Isle of Man	IM	IMN	833
India Flag
India	IN	IND	356
Iraq Flag
Iraq	IQ	IRQ	368
Iran Flag
Iran	IR	IRN	364
Iceland Flag
Iceland	IS	ISL	352
Italy Flag
Italy	IT	ITA	380
Jersey Flag
Jersey	JE	JEY	832
Jamaica Flag
Jamaica	JM	JAM	388
Jordan Flag
Jordan	JO	JOR	400
Japan Flag
Japan	JP	JPN	392
Kenya Flag
Kenya	KE	KEN	404
Kyrgyzstan Flag
Kyrgyzstan	KG	KGZ	417
Cambodia Flag
Cambodia	KH	KHM	116
Kiribati Flag
Kiribati	KI	KIR	296
Comoros Flag
Comoros	KM	COM	174
Saint Kitts and Nevis Flag
Saint Kitts and Nevis	KN	KNA	659
North Korea Flag
North Korea	KP	PRK	408
South Korea Flag
South Korea	KR	KOR	410
Kuwait Flag
Kuwait	KW	KWT	414
Cayman Islands Flag
Cayman Islands	KY	CYM	136
Kazakhstan Flag
Kazakhstan	KZ	KAZ	398
Laos Flag
Laos	LA	LAO	418
Lebanon Flag
Lebanon	LB	LBN	422
Saint Lucia Flag
Saint Lucia	LC	LCA	662
Liechtenstein Flag
Liechtenstein	LI	LIE	438
Sri Lanka Flag
Sri Lanka	LK	LKA	144
Liberia Flag
Liberia	LR	LBR	430
Lesotho Flag
Lesotho	LS	LSO	426
Lithuania Flag
Lithuania	LT	LTU	440
Luxembourg Flag
Luxembourg	LU	LUX	442
Latvia Flag
Latvia	LV	LVA	428
Libya Flag
Libya	LY	LBY	434
Morocco Flag
Morocco	MA	MAR	504
Monaco Flag
Monaco	MC	MCO	492
Moldova Flag
Moldova	MD	MDA	498
Montenegro Flag
Montenegro	ME	MNE	499
Saint Martin Flag
Saint Martin	MF	MAF	663
Madagascar Flag
Madagascar	MG	MDG	450
Marshall Islands Flag
Marshall Islands	MH	MHL	584
North Macedonia Flag
North Macedonia	MK	MKD	807
Mali Flag
Mali	ML	MLI	466
Myanmar Flag
Myanmar	MM	MMR	104
Mongolia Flag
Mongolia	MN	MNG	496
Macau Flag
Macau	MO	MAC	446
Northern Mariana Islands Flag
Northern Mariana Islands	MP	MNP	580
Martinique Flag
Martinique	MQ	MTQ	474
Mauritania Flag
Mauritania	MR	MRT	478
Montserrat Flag
Montserrat	MS	MSR	500
Malta Flag
Malta	MT	MLT	470
Mauritius Flag
Mauritius	MU	MUS	480
Maldives Flag
Maldives	MV	MDV	462
Malawi Flag
Malawi	MW	MWI	454
Mexico Flag
Mexico	MX	MEX	484
Malaysia Flag
Malaysia	MY	MYS	458
Mozambique Flag
Mozambique	MZ	MOZ	508
Namibia Flag
Namibia	NA	NAM	516
New Caledonia Flag
New Caledonia	NC	NCL	540
Niger Flag
Niger	NE	NER	562
Nigeria Flag
Nigeria	NG	NGA	566
Nicaragua Flag
Nicaragua	NI	NIC	558
Netherlands Flag
Netherlands	NL	NLD	528
Norway Flag
Norway	NO	NOR	578
Nepal Flag
Nepal	NP	NPL	524
Nauru Flag
Nauru	NR	NRU	520
Niue Flag
Niue	NU	NIU	570
New Zealand Flag
New Zealand	NZ	NZL	554
Oman Flag
Oman	OM	OMN	512
Panama Flag
Panama	PA	PAN	591
Peru Flag
Peru	PE	PER	604
French Polynesia Flag
French Polynesia	PF	PYF	258
Papua New Guinea Flag
Papua New Guinea	PG	PNG	598
Philippines Flag
Philippines	PH	PHL	608
Pakistan Flag
Pakistan	PK	PAK	586
Poland Flag
Poland	PL	POL	616
Saint Pierre and Miquelon Flag
Saint Pierre and Miquelon	PM	SPM	666
Puerto Rico Flag
Puerto Rico	PR	PRI	630
Palestine Flag
Palestine	PS	PSE	275
Portugal Flag
Portugal	PT	PRT	620
Palau Flag
Palau	PW	PLW	585
Paraguay Flag
Paraguay	PY	PRY	600
Qatar Flag
Qatar	QA	QAT	634
Reunion Flag
Reunion	RE	REU	638
Romania Flag
Romania	RO	ROU	642
Serbia Flag
Serbia	RS	SRB	688
Russia Flag
Russia	RU	RUS	643
Rwanda Flag
Rwanda	RW	RWA	646
Saudi Arabia Flag
Saudi Arabia	SA	SAU	682
Solomon Islands Flag
Solomon Islands	SB	SLB	90
Seychelles Flag
Seychelles	SC	SYC	690
Sudan Flag
Sudan	SD	SDN	729
Sweden Flag
Sweden	SE	SWE	752
Singapore Flag
Singapore	SG	SGP	702
Saint Helena Ascension and Tristan da Cunha Flag
Saint Helena Ascension and Tristan da Cunha	SH	SHN	654
Slovenia Flag
Slovenia	SI	SVN	705
Slovakia Flag
Slovakia	SK	SVK	703
Sierra Leone Flag
Sierra Leone	SL	SLE	694
San Marino Flag
San Marino	SM	SMR	674
Senegal Flag
Senegal	SN	SEN	686
Somalia Flag
Somalia	SO	SOM	706
Suriname Flag
Suriname	SR	SUR	740
South Sudan Flag
South Sudan	SS	SSD	728
Sao Tome and Principe Flag
Sao Tome and Principe	ST	STP	678
El Salvador Flag
El Salvador	SV	SLV	222
Sint Maarten Flag
Sint Maarten	SX	SXM	534
Syria Flag
Syria	SY	SYR	760
Eswatini Flag
Eswatini	SZ	SWZ	748
Turks and Caicos Islands Flag
Turks and Caicos Islands	TC	TCA	796
Chad Flag
Chad	TD	TCD	148
Togo Flag
Togo	TG	TGO	768
Thailand Flag
Thailand	TH	THA	764
Tajikistan Flag
Tajikistan	TJ	TJK	762
Tokelau Flag
Tokelau	TK	TKL	772
Timor-Leste Flag
Timor-Leste	TL	TLS	626
Turkmenistan Flag
Turkmenistan	TM	TKM	795
Tunisia Flag
Tunisia	TN	TUN	788
Tonga Flag
Tonga	TO	TON	776
Turkey Flag
Turkey	TR	TUR	792
Trinidad and Tobago Flag
Trinidad and Tobago	TT	TTO	780
Tuvalu Flag
Tuvalu	TV	TUV	798
Taiwan Flag
Taiwan	TW	TWN	158
Tanzania Flag
Tanzania	TZ	TZA	834
Ukraine Flag
Ukraine	UA	UKR	804
Uganda Flag
Uganda	UG	UGA	800
United States Flag
United States	US	USA	840
Uruguay Flag
Uruguay	UY	URY	858
Uzbekistan Flag
Uzbekistan	UZ	UZB	860
Vatican City Flag
Vatican City	VA	VAT	336
Saint Vincent and the Grenadines Flag
Saint Vincent and the Grenadines	VC	VCT	670
Venezuela Flag
Venezuela	VE	VEN	862
British Virgin Islands Flag
British Virgin Islands	VG	VGB	92
United States Virgin Islands Flag
United States Virgin Islands	VI	VIR	850
Vietnam Flag
Vietnam	VN	VNM	704
Vanuatu Flag
Vanuatu	VU	VUT	548
Wallis and Futuna Flag
Wallis and Futuna	WF	WLF	876
Samoa Flag
Samoa	WS	WSM	882
Yemen Flag
Yemen	YE	YEM	887
Mayotte Flag
Mayotte	YT	MYT	175
South Africa Flag
South Africa	ZA	ZAF	710
Zambia Flag
Zambia	ZM	ZMB	894
Zimbabwe Flag
Zimbabwe	ZW	ZWE	716"""

codes = []
for line in data.split("\n"):
    parts = line.split("\t")
    if len(parts) >= 3:
        # e.g., United Arab Emirates    AE    ARE    784
        code = parts[1].strip()
        if len(code) == 2 and code.isupper():
            codes.append(code)
    elif len(parts) == 3 and not line.endswith("Flag"):
        # wait "AD AND 20"
        if line.startswith("AD\tAND\t20"):
            codes.append("AD")

codes = list(set(codes))
# move US to front
if "US" in codes:
    codes.remove("US")
codes.sort()
codes.insert(0, "US")

print(','.join(f'"{c}"' for c in codes))
