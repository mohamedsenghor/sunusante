import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './legal-guardian.reducer';

export const LegalGuardianDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const legalGuardianEntity = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="legalGuardianDetailsHeading">
          <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.detail.title">LegalGuardian</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{legalGuardianEntity.id}</dd>
          <dt>
            <span id="guardianType">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.guardianType">Guardian Type</Translate>
            </span>
          </dt>
          <dd>{legalGuardianEntity.guardianType}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{legalGuardianEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {legalGuardianEntity.createdDate ? (
              <TextFormat value={legalGuardianEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedBy">
                Last Modified By
              </Translate>
            </span>
          </dt>
          <dd>{legalGuardianEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedDate">
                Last Modified Date
              </Translate>
            </span>
          </dt>
          <dd>
            {legalGuardianEntity.lastModifiedDate ? (
              <TextFormat value={legalGuardianEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.dependent">Dependent</Translate>
          </dt>
          <dd>{legalGuardianEntity.dependent ? legalGuardianEntity.dependent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/legal-guardian" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/legal-guardian/${legalGuardianEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LegalGuardianDetail;
