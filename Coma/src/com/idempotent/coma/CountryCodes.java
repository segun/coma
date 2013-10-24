package com.idempotent.coma;

import com.idempotent.coma.stringshelper.MStrings;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class contains a list of all countries and their two-letter codes(as at Oct 2013)
 * It also contains utility methods for getting the country and/or the code
 *
 * @author aardvocate
 */
public class CountryCodes {

    /**
     * Getter for the list of countries and their codes separated by ;
     * @return The countries and their codes as a ; separated string
     */
    public String getCountryCodes() {
        return countryCodes;
    }

    /**
     * Given the two-letter code, get the country
     * @param code - The country two-letter code. e.g United States of America is US
     * @return The full name of the country
     */
    public String getCountry(String code) {        
        List<String> lines = MStrings.splitString(countryCodes, "\n");
        for (String line : lines) {
            List<String> cc = MStrings.splitString(line, ";");
            if (cc.size() == 2) {
                if (cc.get(1).equalsIgnoreCase(code)) {
                    return cc.get(0);
                }
            }
        }
        return "";
    }
    
    /**
     * Given the full name of the country, get the two-letter code
     * @param country - The full name of the counntry
     * @return The country two-letter code
     */
    private String getCountryCode(String country) {        
        List<String> lines = MStrings.splitString(countryCodes, "\n");
        for (String line : lines) {
            List<String> cc = MStrings.splitString(line, ";");
            if (cc.size() == 2) {
                if (cc.get(0).equalsIgnoreCase(country)) {
                    return cc.get(1);
                }
            }
        }
        return "";
    }
    
    private final String countryCodes =
            "AFGHANISTAN;AF\n"
            + "ALBANIA;AL\n"
            + "ALGERIA;DZ\n"
            + "AMERICAN SAMOA;AS\n"
            + "ANDORRA;AD\n"
            + "ANGOLA;AO\n"
            + "ANGUILLA;AI\n"
            + "ANTARCTICA;AQ\n"
            + "ANTIGUA AND BARBUDA;AG\n"
            + "ARGENTINA;AR\n"
            + "ARMENIA;AM\n"
            + "ARUBA;AW\n"
            + "AUSTRALIA;AU\n"
            + "AUSTRIA;AT\n"
            + "AZERBAIJAN;AZ\n"
            + "BAHAMAS;BS\n"
            + "BAHRAIN;BH\n"
            + "BANGLADESH;BD\n"
            + "BARBADOS;BB\n"
            + "BELARUS;BY\n"
            + "BELGIUM;BE\n"
            + "BELIZE;BZ\n"
            + "BENIN;BJ\n"
            + "BERMUDA;BM\n"
            + "BHUTAN;BT\n"
            + "BOLIVIA, PLURINATIONAL STATE OF;BO\n"
            + "BONAIRE, SINT EUSTATIUS AND SABA;BQ\n"
            + "BOSNIA AND HERZEGOVINA;BA\n"
            + "BOTSWANA;BW\n"
            + "BOUVET ISLAND;BV\n"
            + "BRAZIL;BR\n"
            + "BRITISH INDIAN OCEAN TERRITORY;IO\n"
            + "BRUNEI DARUSSALAM;BN\n"
            + "BULGARIA;BG\n"
            + "BURKINA FASO;BF\n"
            + "BURUNDI;BI\n"
            + "CAMBODIA;KH\n"
            + "CAMEROON;CM\n"
            + "CANADA;CA\n"
            + "CAPE VERDE;CV\n"
            + "CAYMAN ISLANDS;KY\n"
            + "CENTRAL AFRICAN REPUBLIC;CF\n"
            + "CHAD;TD\n"
            + "CHILE;CL\n"
            + "CHINA;CN\n"
            + "CHRISTMAS ISLAND;CX\n"
            + "COCOS (KEELING) ISLANDS;CC\n"
            + "COLOMBIA;CO\n"
            + "COMOROS;KM\n"
            + "CONGO;CG\n"
            + "CONGO, THE DEMOCRATIC REPUBLIC OF THE;CD\n"
            + "COOK ISLANDS;CK\n"
            + "COSTA RICA;CR\n"
            + "COTE D'IVOIRE;CI\n"
            + "CROATIA;HR\n"
            + "CUBA;CU\n"
            + "CURA;CW\n"
            + "CYPRUS;CY\n"
            + "CZECH REPUBLIC;CZ\n"
            + "DENMARK;DK\n"
            + "DJIBOUTI;DJ\n"
            + "DOMINICA;DM\n"
            + "DOMINICAN REPUBLIC;DO\n"
            + "ECUADOR;EC\n"
            + "EGYPT;EG\n"
            + "EL SALVADOR;SV\n"
            + "EQUATORIAL GUINEA;GQ\n"
            + "ERITREA;ER\n"
            + "ESTONIA;EE\n"
            + "ETHIOPIA;ET\n"
            + "FALKLAND ISLANDS (MALVINAS);FK\n"
            + "FAROE ISLANDS;FO\n"
            + "FIJI;FJ\n"
            + "FINLAND;FI\n"
            + "FRANCE;FR\n"
            + "FRENCH GUIANA;GF\n"
            + "FRENCH POLYNESIA;PF\n"
            + "FRENCH SOUTHERN TERRITORIES;TF\n"
            + "GABON;GA\n"
            + "GAMBIA;GM\n"
            + "GEORGIA;GE\n"
            + "GERMANY;DE\n"
            + "GHANA;GH\n"
            + "GIBRALTAR;GI\n"
            + "GREECE;GR\n"
            + "GREENLAND;GL\n"
            + "GRENADA;GD\n"
            + "GUADELOUPE;GP\n"
            + "GUAM;GU\n"
            + "GUATEMALA;GT\n"
            + "GUERNSEY;GG\n"
            + "GUINEA;GN\n"
            + "GUINEA-BISSAU;GW\n"
            + "GUYANA;GY\n"
            + "HAITI;HT\n"
            + "HEARD ISLAND AND MCDONALD ISLANDS;HM\n"
            + "HOLY SEE (VATICAN CITY STATE);VA\n"
            + "HONDURAS;HN\n"
            + "HONG KONG;HK\n"
            + "HUNGARY;HU\n"
            + "ICELAND;IS\n"
            + "INDIA;IN\n"
            + "INDONESIA;ID\n"
            + "IRAN, ISLAMIC REPUBLIC OF;IR\n"
            + "IRAQ;IQ\n"
            + "IRELAND;IE\n"
            + "ISLE OF MAN;IM\n"
            + "ISRAEL;IL\n"
            + "ITALY;IT\n"
            + "JAMAICA;JM\n"
            + "JAPAN;JP\n"
            + "JERSEY;JE\n"
            + "JORDAN;JO\n"
            + "KAZAKHSTAN;KZ\n"
            + "KENYA;KE\n"
            + "KIRIBATI;KI\n"
            + "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF;KP\n"
            + "KOREA, REPUBLIC OF;KR\n"
            + "KUWAIT;KW\n"
            + "KYRGYZSTAN;KG\n"
            + "LAO PEOPLE'S DEMOCRATIC REPUBLIC;LA\n"
            + "LATVIA;LV\n"
            + "LEBANON;LB\n"
            + "LESOTHO;LS\n"
            + "LIBERIA;LR\n"
            + "LIBYA;LY\n"
            + "LIECHTENSTEIN;LI\n"
            + "LITHUANIA;LT\n"
            + "LUXEMBOURG;LU\n"
            + "MACAO;MO\n"
            + "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF;MK\n"
            + "MADAGASCAR;MG\n"
            + "MALAWI;MW\n"
            + "MALAYSIA;MY\n"
            + "MALDIVES;MV\n"
            + "MALI;ML\n"
            + "MALTA;MT\n"
            + "MARSHALL ISLANDS;MH\n"
            + "MARTINIQUE;MQ\n"
            + "MAURITANIA;MR\n"
            + "MAURITIUS;MU\n"
            + "MAYOTTE;YT\n"
            + "MEXICO;MX\n"
            + "MICRONESIA, FEDERATED STATES OF;FM\n"
            + "MOLDOVA, REPUBLIC OF;MD\n"
            + "MONACO;MC\n"
            + "MONGOLIA;MN\n"
            + "MONTENEGRO;ME\n"
            + "MONTSERRAT;MS\n"
            + "MOROCCO;MA\n"
            + "MOZAMBIQUE;MZ\n"
            + "MYANMAR;MM\n"
            + "NAMIBIA;NA\n"
            + "NAURU;NR\n"
            + "NEPAL;NP\n"
            + "NETHERLANDS;NL\n"
            + "NEW CALEDONIA;NC\n"
            + "NEW ZEALAND;NZ\n"
            + "NICARAGUA;NI\n"
            + "NIGER;NE\n"
            + "NIGERIA;NG\n"
            + "NIUE;NU\n"
            + "NORFOLK ISLAND;NF\n"
            + "NORTHERN MARIANA ISLANDS;MP\n"
            + "NORWAY;NO\n"
            + "OMAN;OM\n"
            + "PAKISTAN;PK\n"
            + "PALAU;PW\n"
            + "PALESTINE, STATE OF;PS\n"
            + "PANAMA;PA\n"
            + "PAPUA NEW GUINEA;PG\n"
            + "PARAGUAY;PY\n"
            + "PERU;PE\n"
            + "PHILIPPINES;PH\n"
            + "PITCAIRN;PN\n"
            + "POLAND;PL\n"
            + "PORTUGAL;PT\n"
            + "PUERTO RICO;PR\n"
            + "QATAR;QA\n"
            + "RE-UNION;RE\n"
            + "ROMANIA;RO\n"
            + "RUSSIAN FEDERATION;RU\n"
            + "RWANDA;RW\n"
            + "SAINT BARTHOLEMY;BL\n"
            + "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA;SH\n"
            + "SAINT KITTS AND NEVIS;KN\n"
            + "SAINT LUCIA;LC\n"
            + "SAINT MARTIN (FRENCH PART);MF\n"
            + "SAINT PIERRE AND MIQUELON;PM\n"
            + "SAINT VINCENT AND THE GRENADINES;VC\n"
            + "SAMOA;WS\n"
            + "SAN MARINO;SM\n"
            + "SAO TOME AND PRINCIPE;ST\n"
            + "SAUDI ARABIA;SA\n"
            + "SENEGAL;SN\n"
            + "SERBIA;RS\n"
            + "SEYCHELLES;SC\n"
            + "SIERRA LEONE;SL\n"
            + "SINGAPORE;SG\n"
            + "SINT MAARTEN (DUTCH PART);SX\n"
            + "SLOVAKIA;SK\n"
            + "SLOVENIA;SI\n"
            + "SOLOMON ISLANDS;SB\n"
            + "SOMALIA;SO\n"
            + "SOUTH AFRICA;ZA\n"
            + "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS;GS\n"
            + "SOUTH SUDAN;SS\n"
            + "SPAIN;ES\n"
            + "SRI LANKA;LK\n"
            + "SUDAN;SD\n"
            + "SURINAME;SR\n"
            + "SVALBARD AND JAN MAYEN;SJ\n"
            + "SWAZILAND;SZ\n"
            + "SWEDEN;SE\n"
            + "SWITZERLAND;CH\n"
            + "SYRIAN ARAB REPUBLIC;SY\n"
            + "TAIWAN, PROVINCE OF CHINA;TW\n"
            + "TAJIKISTAN;TJ\n"
            + "TANZANIA, UNITED REPUBLIC OF;TZ\n"
            + "THAILAND;TH\n"
            + "TIMOR-LESTE;TL\n"
            + "TOGO;TG\n"
            + "TOKELAU;TK\n"
            + "TONGA;TO\n"
            + "TRINIDAD AND TOBAGO;TT\n"
            + "TUNISIA;TN\n"
            + "TURKEY;TR\n"
            + "TURKMENISTAN;TM\n"
            + "TURKS AND CAICOS ISLANDS;TC\n"
            + "TUVALU;TV\n"
            + "UGANDA;UG\n"
            + "UKRAINE;UA\n"
            + "UNITED ARAB EMIRATES;AE\n"
            + "UNITED KINGDOM;GB\n"
            + "UNITED STATES;US\n"
            + "UNITED STATES MINOR OUTLYING ISLANDS;UM\n"
            + "URUGUAY;UY\n"
            + "UZBEKISTAN;UZ\n"
            + "VANUATU;VU\n"
            + "VENEZUELA, BOLIVARIAN REPUBLIC OF;VE\n"
            + "VIET NAM;VN\n"
            + "VIRGIN ISLANDS, BRITISH;VG\n"
            + "VIRGIN ISLANDS, U.S.;VI\n"
            + "WALLIS AND FUTUNA;WF\n"
            + "WESTERN SAHARA;EH\n"
            + "YEMEN;YE\n"
            + "ZAMBIA;ZM\n"
            + "ZIMBABWE;ZW";
}
