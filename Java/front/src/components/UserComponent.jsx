import React, { useState, useEffect } from 'react';
import { connect } from "react-redux";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons'
import {useNavigate, useParams} from 'react-router'
import { Button, Form } from 'react-bootstrap';
import { alertActions } from '../utils/Rdx';

const UserComponent = props => {

    const nav = useNavigate();
    const [hidden, setHidden] = useState(true);
    const [login, setLogin] = useState('');
    const [email, setEmail] = useState('');
    const id = useParams().id;

    const handleSubmit = e => {
        e.preventDefault();
        
        let err = null;

        if (login == "" || email == "") {
            err = 'Пустое поле ввода'
        }

        if (err != null) {
            props.dispatch(alertActions.error(err));
        } 
        else {
            if (id == -1) {
            let user = {
                login: login,
                email: email
            }
            BackendService.createUser(user)
            .then(nav('/users'))
            .catch(() => {
            })

        } 
        else {
            let user = {
                id: id,
                login: login,
                email: email
            }
            BackendService.updateUser(user)
            .then(nav('/users'))
            .catch(() => {
            })
        }
        }
    }

    const onBackButtonClicked = () => {
        nav('/users');
    }

    const getUser = () => {        
        if (id != -1) {
            BackendService.retrieveUser(id).then(resp => {
                setLogin(resp.data.login);
                setEmail(resp.data.email);
                setHidden(false);
            })
            .catch(() => {
                setHidden(true)
            })
        } 
        else {
            setHidden(false)
        }
    }

    useEffect(() => {
            getUser();
        }, [])
    
    if (hidden)
        return null;
    return (
         <div className="m-4">
            <div className="row my-2">
                <div className='col'>
                    <h3>Пользователь</h3>
                </div>
                <div className="btn-toolbar col">
                    <div className="btn-group ms-auto">
                        <button className="btn btn-outline-secondary"
                                onClick={onBackButtonClicked}>
                            <FontAwesomeIcon icon={faArrowLeft} />{' '}Назад
                        </button>
                    </div>
                </div>
                
            </div>
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>
                        Логин
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={login} 
                        name="login" 
                        onChange={(e) => setLogin(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                    <Form.Label>
                        E-Mail
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={email} 
                        name="email" 
                        onChange={(e) => setEmail(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                </Form.Group>
                <Button type='submit' variant='outline-secondary'>
                    <FontAwesomeIcon icon={faSave}/>{' '}Сохранить
                </Button>
            </Form>
        </div>
    );
}

export default connect()(UserComponent);