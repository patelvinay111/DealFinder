--
-- PostgreSQL database dump
--

-- Dumped from database version 12.6
-- Dumped by pg_dump version 12.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: deals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deals (
    property_name character varying NOT NULL,
    rent_to_price double precision,
    cash_flow double precision,
    min_equity_earn double precision,
    asking_price double precision,
    rent double precision,
    vacancy_cost double precision,
    property_tax double precision,
    property_managent double precision,
    leasing_fee double precision,
    insurance double precision,
    maintenance_cost double precision,
    capital_reserve double precision,
    mortgage_payment double precision,
    noi double precision
);


ALTER TABLE public.deals OWNER TO postgres;

--
-- Name: leads; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.leads (
    property_id character varying NOT NULL,
    street character varying,
    zipcode character varying,
    address character varying,
    city character varying,
    area character varying,
    asking_price double precision,
    property_type character varying,
    bed double precision,
    bath double precision,
    construction double precision,
    lot double precision,
    url character varying,
    is_new_construction boolean,
    last_update character varying,
    prop_status character varying,
    list_date character varying,
    photo character varying,
    baths_half double precision,
    baths_full double precision,
    photo_count double precision,
    lat double precision,
    lon double precision,
    is_new_listing boolean
);


ALTER TABLE public.leads OWNER TO postgres;

--
-- Name: daily_hunt; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.daily_hunt AS
 SELECT d.property_name,
    l.area,
    d.rent,
    d.cash_flow,
    d.rent_to_price,
    l.url
   FROM (public.deals d
     LEFT JOIN public.leads l ON (((d.property_name)::text = (l.address)::text)))
  WHERE (((l.city)::text <> (l.area)::text) AND (d.rent_to_price > (0.65)::double precision) AND (d.rent_to_price < (0.85)::double precision) AND (d.rent > (1000)::double precision) AND (d.rent < (1450)::double precision))
  ORDER BY d.rent_to_price DESC;


ALTER TABLE public.daily_hunt OWNER TO postgres;

--
-- Name: deals deals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deals
    ADD CONSTRAINT deals_pkey PRIMARY KEY (property_name);


--
-- Name: leads leads_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leads
    ADD CONSTRAINT leads_pkey PRIMARY KEY (property_id);


--
-- PostgreSQL database dump complete
--

