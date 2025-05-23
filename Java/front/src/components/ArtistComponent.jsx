import React, { useState, useEffect } from 'react';
import { connect } from "react-redux";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons'
import {useNavigate, useParams} from 'react-router'
import { Button, Form } from 'react-bootstrap';
import { alertActions } from '../utils/Rdx';

const ArtistComponent = props => {

    const nav = useNavigate();
    const [hidden, setHidden] = useState(true);
    const [name, setName] = useState('');
    const [age, setAge] = useState('');
    const [countryId, setCountryId] = useState('');
    const [countries, setCountries] = useState([]);
    const id = useParams().id;

    const handleSubmit = e => {
        e.preventDefault();
        
        let err = null;
        
        if (name == "" || age == "") {
            err = 'Пустое поле ввода'
        }

        if (countryId == "") {
            err = 'Выберите страну'
        }

        if (err != null) {
            props.dispatch(alertActions.error(err));
        } 
        else {
            if (id == -1) {
                let artist = {
                    name: name,
                    age: age,
                    country: {
                        id: countryId
                    }
                }
                BackendService.createArtist(artist)
                .then(nav('/artists'))
                .catch(() => {
                
                })
            } 
            else {
                let artist = {
                    id: id,
                    name: name,
                    age: age,
                    country: {
                        id: countryId
                    }
                }
                BackendService.updateArtist(artist)
                .then(nav('/artists'))
                .catch(() => {

                })
            }
        }
    }

    const onBackButtonClicked = () => {
        nav('/artists');
    }

    const getArtist = () => {        
        if (id != -1) {
            BackendService.retrieveArtist(id).then(resp => {
                setName(resp.data.name);
                if (resp.data.country !== null) {
                    setCountryId(resp.data.country.id);
                }
                setAge(resp.data.age);
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
            BackendService.retrieveAllCountries(0, 1000)
            .then(resp => {
                setCountries(resp.data.content);
            })
            getArtist();
        }, [])
    
    if (hidden)
        return null;
    return (
         <div className="m-4">
            <div className="row my-2">
                <div className='col'>
                    <h3>Художник</h3>
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
                        Имя
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={name} 
                        name="name" 
                        onChange = {(e) => setName(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                    <Form.Label>
                        Век
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={age} 
                        name="age" 
                        onChange = {(e) => setAge(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                    <Form.Label>
                        Страна
                    </Form.Label>
                    <Form.Select 
                        value={countryId} 
                        onChange = {(e) => setCountryId(e.target.value)}>
                            <option value="">Выберите страну</option>
                                {countries && countries.length > 0 ? (
                                    countries.map(country => (
                                        <option key={country.id} value={country.id}>
                                            {country.name}
                                        </option>
                                    ))
                                ) : (
                                    <option value="" disabled>Загрузка стран...</option>
                                )}
                    </Form.Select>
                </Form.Group>
                <Button type='submit' variant='outline-secondary'>
                    <FontAwesomeIcon icon={faSave}/>{' '}Сохранить
                </Button>
            </Form>
        </div>
    );
}

export default connect()(ArtistComponent);