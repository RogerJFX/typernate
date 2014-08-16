--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.14
-- Dumped by pg_dump version 9.1.14
-- Started on 2014-08-16 16:57:25 CEST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1965 (class 1262 OID 1799491)
-- Name: typernate; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE typernate WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'de_DE.UTF-8' LC_CTYPE = 'de_DE.UTF-8';


\connect typernate

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 170 (class 3079 OID 11716)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1967 (class 0 OID 0)
-- Dependencies: 170
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 493 (class 1247 OID 1799513)
-- Dependencies: 5 161
-- Name: type_test_int_string; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE type_test_int_string AS (
	id integer,
	str character varying
);


--
-- TOC entry 499 (class 1247 OID 1799519)
-- Dependencies: 5 163
-- Name: type_test; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE type_test AS (
	id integer,
	iname character varying,
	test_data type_test_int_string[]
);


--
-- TOC entry 496 (class 1247 OID 1799516)
-- Dependencies: 5 162
-- Name: type_test_single; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE type_test_single AS (
	id integer,
	iname character varying,
	test_data type_test_int_string
);


--
-- TOC entry 182 (class 1255 OID 1799900)
-- Dependencies: 517 5
-- Name: func_typeinsert(anyelement); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION func_typeinsert(_element anyelement) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE 
	seqval integer;
BEGIN
	select nextval('tbl_typetest_id_seq') into seqval;
	insert into tbl_typetest (id, itype) values(seqval, _element);
	return seqval;
END;
$$;


--
-- TOC entry 183 (class 1255 OID 1799999)
-- Dependencies: 517 5
-- Name: func_typeinsertsingle(anyelement); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION func_typeinsertsingle(_element anyelement) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE 
	seqval integer;
BEGIN
	select nextval('tbl_typetest_single_id_seq') into seqval;
	insert into tbl_typetest_single (id, itype) values(seqval, _element);
	return seqval;
	
END;
$$;


SET default_with_oids = false;

--
-- TOC entry 165 (class 1259 OID 1799522)
-- Dependencies: 5 499
-- Name: tbl_typetest; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE tbl_typetest (
    id integer NOT NULL,
    itype type_test
);


--
-- TOC entry 164 (class 1259 OID 1799520)
-- Dependencies: 165 5
-- Name: tbl_typetest_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tbl_typetest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1968 (class 0 OID 0)
-- Dependencies: 164
-- Name: tbl_typetest_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tbl_typetest_id_seq OWNED BY tbl_typetest.id;


--
-- TOC entry 167 (class 1259 OID 1799533)
-- Dependencies: 5 496
-- Name: tbl_typetest_single; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE tbl_typetest_single (
    id integer NOT NULL,
    itype type_test_single
);


--
-- TOC entry 166 (class 1259 OID 1799531)
-- Dependencies: 5 167
-- Name: tbl_typetest_single_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tbl_typetest_single_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1969 (class 0 OID 0)
-- Dependencies: 166
-- Name: tbl_typetest_single_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tbl_typetest_single_id_seq OWNED BY tbl_typetest_single.id;


--
-- TOC entry 169 (class 1259 OID 1800966)
-- Dependencies: 500 5
-- Name: tbl_typetest_with_list; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE tbl_typetest_with_list (
    id integer NOT NULL,
    itype type_test[]
);


--
-- TOC entry 168 (class 1259 OID 1800964)
-- Dependencies: 5 169
-- Name: tbl_typetest_with_list_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tbl_typetest_with_list_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1970 (class 0 OID 0)
-- Dependencies: 168
-- Name: tbl_typetest_with_list_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tbl_typetest_with_list_id_seq OWNED BY tbl_typetest_with_list.id;


--
-- TOC entry 1851 (class 2604 OID 1799525)
-- Dependencies: 165 164 165
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest ALTER COLUMN id SET DEFAULT nextval('tbl_typetest_id_seq'::regclass);


--
-- TOC entry 1852 (class 2604 OID 1799536)
-- Dependencies: 167 166 167
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest_single ALTER COLUMN id SET DEFAULT nextval('tbl_typetest_single_id_seq'::regclass);


--
-- TOC entry 1853 (class 2604 OID 1800969)
-- Dependencies: 169 168 169
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest_with_list ALTER COLUMN id SET DEFAULT nextval('tbl_typetest_with_list_id_seq'::regclass);


--
-- TOC entry 1855 (class 2606 OID 1799530)
-- Dependencies: 165 165 1962
-- Name: pk_test_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest
    ADD CONSTRAINT pk_test_type PRIMARY KEY (id);


--
-- TOC entry 1859 (class 2606 OID 1800974)
-- Dependencies: 169 169 1962
-- Name: pk_test_type_list; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest_with_list
    ADD CONSTRAINT pk_test_type_list PRIMARY KEY (id);


--
-- TOC entry 1857 (class 2606 OID 1799541)
-- Dependencies: 167 167 1962
-- Name: pk_test_type_single; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_typetest_single
    ADD CONSTRAINT pk_test_type_single PRIMARY KEY (id);


-- Completed on 2014-08-16 16:57:25 CEST

--
-- PostgreSQL database dump complete
--

