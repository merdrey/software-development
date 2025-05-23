import React, { useState, useEffect } from 'react';
import { connect } from "react-redux";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons'
import {useNavigate, useParams} from 'react-router'
import { Button, Form } from 'react-bootstrap';
import { alertActions } from '../utils/Rdx';

const MuseumComponent = props => {

    const nav = useNavigate();
    const [hidden, setHidden] = useState(true);
    const [name, setName] = useState('');
    const [location, setLocation] = useState('');
    const id = useParams().id;

    const handleSubmit = e => {
        e.preventDefault();
        
        let err = null;

        if (name == "" || location == "") {
            err = 'Пустое поле ввода'
        }

        if (err != null) {
            props.dispatch(alertActions.error(err));
        } 
        else {
            if (id == -1) {
            let museum = {
                name: name,
                location: location
            }
            BackendService.createMuseum(museum)
            .then(nav('/museums'))
            .catch(() => {
            })

        } 
        else {
            let museum = {
                id: id,
                name: name,
                location: location
            }
            BackendService.updateMuseum(museum)
            .then(nav('/museums'))
            .catch(() => {
            })
        }
        }
    }

    const onBackButtonClicked = () => {
        nav('/museums');
    }

    const getMuseum = () => {        
        if (id != -1) {
            BackendService.retrieveMuseum(id).then(resp => {
                setName(resp.data.name);
                setLocation(resp.data.location);
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
            getMuseum();
        }, [])
    
    if (hidden)
        return null;
    return (
         <div className="m-4">
            <div className="row my-2">
                <div className='col'>
                    <h3>Музей</h3>
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
                        Название
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={name} 
                        name="name" 
                        onChange={(e) => setName(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                    <Form.Label>
                        Расположение
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={location} 
                        name="location" 
                        onChange={(e) => setLocation(e.target.value)} 
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

export default connect()(MuseumComponent);