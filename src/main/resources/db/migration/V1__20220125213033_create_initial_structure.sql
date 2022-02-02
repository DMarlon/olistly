CREATE DOMAIN status AS smallint NOT NULL;
COMMENT ON DOMAIN status IS '1-enable 2-disable';

CREATE TABLE IF NOT EXISTS syspreusers
(
    pus_id serial NOT NULL,
    pus_login character varying NOT NULL,
    pus_name character varying NOT NULL,
    pus_password character varying NOT NULL,
    pus_dateexpiration timestamp without time zone NOT NULL,
    pus_activationhash character varying NOT NULL,
    PRIMARY KEY (pus_id),
    CONSTRAINT pus_login UNIQUE (pus_login)
);

CREATE TABLE IF NOT EXISTS sysusers
(
    use_id serial NOT NULL,
    use_login character varying NOT NULL,
    use_name character varying NOT NULL,
    use_status status DEFAULT 1,
    PRIMARY KEY (use_id),
    CONSTRAINT use_login UNIQUE (use_login)
);

CREATE TABLE IF NOT EXISTS sysusercredentials
(
    cre_id serial NOT NULL,
    cre_user_id integer NOT NULL,
    cre_password character varying NOT NULL,
    cre_passwordresethash character varying(40),
    cre_passwordresetexpiration timestamp without time zone,
    PRIMARY KEY (cre_id),
    CONSTRAINT cre_user_id_uni UNIQUE (cre_user_id),
    CONSTRAINT fk_sysusercredentials_sysusers_use_id FOREIGN KEY (cre_user_id) REFERENCES sysusers (use_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID
);

CREATE TABLE IF NOT EXISTS syssessions
(
    ses_id serial NOT NULL,
    ses_user_id integer NOT NULL,
    ses_token character varying NOT NULL,
    ses_devicename character varying,
    ses_remoteaddress character varying,
    ses_lastaccess timestamp without time zone NOT NULL,
    ses_expiration timestamp without time zone NOT NULL,
    PRIMARY KEY (ses_id),
    CONSTRAINT fk_syssessions_sysusers_use_id FOREIGN KEY (ses_user_id) REFERENCES sysusers (use_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID
);

CREATE TABLE IF NOT EXISTS syssessions
(
    ses_id serial NOT NULL,
    ses_user_id integer NOT NULL,
    ses_token character varying NOT NULL,
    ses_devicename character varying,
    ses_remoteaddress character varying,
    ses_lastaccess timestamp without time zone NOT NULL,
    ses_expiration timestamp without time zone NOT NULL,
    PRIMARY KEY (ses_id),
    CONSTRAINT fk_syssessions_sysusers_use_id FOREIGN KEY (ses_user_id) REFERENCES sysusers (use_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID
);


CREATE TABLE IF NOT EXISTS urladdresses
(
    uad_id serial NOT NULL,
    uad_user_id integer NOT NULL,
    uad_originalurl character varying NOT NULL,
    uad_shortenedhash character varying,
    uad_datecreated timestamp without time zone NOT NULL,
    uad_status status DEFAULT 1,
    PRIMARY KEY (uad_id),
    CONSTRAINT fk_urladdresses_sysusers_use_id FOREIGN KEY (uad_user_id) REFERENCES sysusers (use_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID
);

CREATE TABLE IF NOT EXISTS urladdressviews
(
    uav_id serial NOT NULL,
    uav_urladdress_id integer NOT NULL,
    uav_devicename character varying,
    uav_remoteaddress character varying,
    uav_dateaccess timestamp without time zone NOT NULL,
    PRIMARY KEY (uav_id),
    CONSTRAINT fk_urladdressviews_urladdresses_uad_id FOREIGN KEY (uav_urladdress_id) REFERENCES urladdresses (uad_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID
);

 