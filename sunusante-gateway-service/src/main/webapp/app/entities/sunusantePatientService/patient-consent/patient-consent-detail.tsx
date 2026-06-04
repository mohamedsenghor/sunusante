import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient-consent.reducer';

export const PatientConsentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientConsentEntity = useAppSelector(state => state.sunusantegatewayservice.patientConsent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientConsentDetailsHeading">
          <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.detail.title">PatientConsent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{patientConsentEntity.id}</dd>
          <dt>
            <span id="patientPseudo">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.patientPseudo">
                Patient Pseudo
              </Translate>
            </span>
          </dt>
          <dd>{patientConsentEntity.patientPseudo}</dd>
          <dt>
            <span id="doctorLogin">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.doctorLogin">Doctor Login</Translate>
            </span>
          </dt>
          <dd>{patientConsentEntity.doctorLogin}</dd>
          <dt>
            <span id="consentDate">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.consentDate">Consent Date</Translate>
            </span>
          </dt>
          <dd>
            {patientConsentEntity.consentDate ? (
              <TextFormat value={patientConsentEntity.consentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {patientConsentEntity.expiryDate ? (
              <TextFormat value={patientConsentEntity.expiryDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.status">Status</Translate>
            </span>
          </dt>
          <dd>{patientConsentEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/patient-consent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient-consent/${patientConsentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientConsentDetail;
