--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2014-08-17 17:04:18 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 16388)
-- Name: tbl_jsontest; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE tbl_jsontest (
    id integer NOT NULL,
    ijson json
);


--
-- TOC entry 170 (class 1259 OID 16386)
-- Name: tbl_jsontest_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tbl_jsontest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 170
-- Name: tbl_jsontest_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tbl_jsontest_id_seq OWNED BY tbl_jsontest.id;


--
-- TOC entry 1905 (class 2604 OID 16391)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_jsontest ALTER COLUMN id SET DEFAULT nextval('tbl_jsontest_id_seq'::regclass);


--
-- TOC entry 2016 (class 0 OID 16388)
-- Dependencies: 171
-- Data for Name: tbl_jsontest; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO tbl_jsontest VALUES (1, '{"a": 1}');
INSERT INTO tbl_jsontest VALUES (2, '{"a":2, "test":"string"}');
INSERT INTO tbl_jsontest VALUES (3, '{"foo": "bar"}');


--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 170
-- Name: tbl_jsontest_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('tbl_jsontest_id_seq', 3, true);


--
-- TOC entry 1907 (class 2606 OID 16396)
-- Name: pk_test_json; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tbl_jsontest
    ADD CONSTRAINT pk_test_json PRIMARY KEY (id);


-- Completed on 2014-08-17 17:04:22 CEST

--
-- PostgreSQL database dump complete
--

