import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-account.reducer';

export const UserAccountDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAccountEntity = useAppSelector(state => state.sunusantegatewayservice.userAccount.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAccountDetailsHeading">
          <Translate contentKey="sunusanteGatewayServiceApp.userAccount.detail.title">UserAccount</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.id}</dd>
          <dt>
            <span id="mfaSecret">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.mfaSecret">Mfa Secret</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.mfaSecret}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.role">Role</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.role}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.createdDate ? (
              <TextFormat value={userAccountEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="sunusanteGatewayServiceApp.userAccount.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.lastModifiedDate ? (
              <TextFormat value={userAccountEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="sunusanteGatewayServiceApp.userAccount.internalUser">Internal User</Translate>
          </dt>
          <dd>{userAccountEntity.internalUser ? userAccountEntity.internalUser.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-account" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-account/${userAccountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAccountDetail;
