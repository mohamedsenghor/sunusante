import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, approveEntity } from './patient-consent.reducer';
import { ConsentStatus } from 'app/shared/model/enumerations/consent-status.model';

export const PatientConsent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const patientConsentList = useAppSelector(state => state.sunusantegatewayservice.patientConsent.entities);
  const loading = useAppSelector(state => state.sunusantegatewayservice.patientConsent.loading);
  const totalItems = useAppSelector(state => state.sunusantegatewayservice.patientConsent.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const handleApprove = id => {
    dispatch(approveEntity(id));
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="patient-consent-heading" data-cy="PatientConsentHeading">
        <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.title">Patient Consents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.refreshListLabel">
              Refresh List
            </Translate>
          </Button>
          <Link to="/patient-consent/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.createLabel">
              Create new Patient Consent
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {patientConsentList && patientConsentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('patientPseudo')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.patientPseudo">
                    Patient Pseudo
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('patientPseudo')} />
                </th>
                <th className="hand" onClick={sort('doctorLogin')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.doctorLogin">
                    Doctor Login
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('doctorLogin')} />
                </th>
                <th className="hand" onClick={sort('consentDate')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.consentDate">
                    Consent Date
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('consentDate')} />
                </th>
                <th className="hand" onClick={sort('expiryDate')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.expiryDate">
                    Expiry Date
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expiryDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {patientConsentList.map((patientConsent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/patient-consent/${patientConsent.id}`} color="link" size="sm">
                      {patientConsent.id}
                    </Button>
                  </td>
                  <td>{patientConsent.patientPseudo}</td>
                  <td>{patientConsent.doctorLogin}</td>
                  <td>
                    {patientConsent.consentDate ? (
                      <TextFormat type="date" value={patientConsent.consentDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {patientConsent.expiryDate ? (
                      <TextFormat type="date" value={patientConsent.expiryDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`sunusanteGatewayServiceApp.ConsentStatus.${patientConsent.status}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {patientConsent.status === ConsentStatus.PENDING && (
                        <Button color="success" size="sm" onClick={() => handleApprove(patientConsent.id)} data-cy="entityApproveButton">
                          <FontAwesomeIcon icon="check" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.approve">Approve</Translate>
                          </span>
                        </Button>
                      )}
                      <Button tag={Link} to={`/patient-consent/${patientConsent.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/patient-consent/${patientConsent.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/patient-consent/${patientConsent.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.notFound">
                No Patient Consents found
              </Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={patientConsentList && patientConsentList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default PatientConsent;
