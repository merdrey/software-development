import React, { useState, useEffect } from 'react';
import { connect } from "react-redux";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons'
import {useNavigate, useParams} from 'react-router'
import { Button, Form } from 'react-bootstrap';
import { alertActions } from '../utils/Rdx';

const CountryComponent = props => {

    const nav = useNavigate();
    const [hidden, setHidden] = useState(true);
    const [name, setName] = useState('');
    const id = useParams().id;

    const handleSubmit = e => {
        e.preventDefault();
        
        let err = null;
        console.log(name == "");
        
        if (name == "") {
            err = 'Пустое поле ввода'
        }

        if (err != null) {
            props.dispatch(alertActions.error(err));
        } 
        else {
            if (id == -1) {
            let country = {
                name: name
            }
            BackendService.createCountry(country)
            .then(resp => {
                console.log(resp.data);
                
                nav('/countries')
            })

        } 
        else {
            let country = {
                id: id,
                name: name
            }
            BackendService.updateCountry(country)
            .then(resp => {
                console.log(resp.data);
                
                nav('/countries')
            })
        }
        }
    }

    const handleChange = (e) => {
        setName(e.target.value);
    }

    const onBackButtonClicked = () => {
        nav('/countries');
    }

    const getCountry = () => {        
        if (id != -1) {
            BackendService.retrieveCountry(id).then(resp => {
                setName(resp.data.name);
                setHidden(false);
            })
            .catch(() => setHidden(true))
        } 
        else {
            setHidden(false)
        }
    }

    useEffect(() => {
            getCountry();
        }, [])
    
    if (hidden)
        return null;
    return (
         <div className="m-4">
            <div className="row my-2">
                <div className='col'>
                    <h3>Страна</h3>
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
                        onChange={handleChange} 
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

export default connect()(CountryComponent);